package handlers

import (
	bingo "KeepAliveBingo/internal/Bingo"
	"KeepAliveBingo/internal/config"
	"fmt"
	"html/template"
	"log"
	"net/http"
	"sync"
	"time"

	"github.com/gorilla/websocket"
)

type WebSocketConnection struct {
	*websocket.Conn
}

type client struct {
	conn          WebSocketConnection
	b             *bingo.Bingo
	message       string
	state         string
	currentNumber int
	mu            sync.Mutex
}

var clients = make(map[WebSocketConnection]*client)
var wsChan = make(chan *client)

var upgradeConnection = websocket.Upgrader{
	ReadBufferSize:  1024,
	WriteBufferSize: 1024,
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

func Home(w http.ResponseWriter, r *http.Request) {
	// TODO: make better design for index page
	homeTemplate, _ := template.ParseFiles("internal/templates/index.html")
	homeTemplate.Execute(w, nil)
}

func Play(w http.ResponseWriter, r *http.Request) {
	ws, err := upgradeConnection.Upgrade(w, r, nil)
	if err != nil {
		log.Println(err)
		return
	}

	conn := WebSocketConnection{ws}

	clients[conn] = &client{
		conn:  conn,
		b:     bingo.NewBingo(),
		state: "init",
	}
	// TODO: Write a message to the client to help them get started
	ws.WriteMessage(1, []byte("Welcome to Bingo!\n You can get the list of commands by sending help command."))

	go ListenForWS(&conn)

}

func ListenForWS(conn *WebSocketConnection) {

	defer func() {
		if r := recover(); r != nil {
			log.Println("Error", r)
		}
	}()

	for {
		_, msg, err := conn.ReadMessage()
		if err != nil {
			log.Println(err)
			return
		}

		client := clients[*conn]
		client.message = string(msg)
		wsChan <- client

	}

}

func ListenToWSChannel(config config.Config) {

	for {
		client := <-wsChan

		switch client.message {
		case "get board":
			if client.state == "init" {
				client.send(client.b.PrintBingoBoard())
				client.state = "board-sent"
			} else {
				client.send("Board already sent if you want to play again send 'reset'")
			}

		case "reset":
			client.b = bingo.NewBingo()
			client.state = "init"
			client.send("Game reset, request the board again with 'get board'")

		case "play":
			if client.state == "board-sent" {
				client.send("Starting game...")
				go func() {
					for i := 0; i < 75; i++ {
						if client.state == "init" {
							break
						}
						number := client.b.BroadcastNumber()
						client.currentNumber = number
						if number <= 15 {
							client.send("B: " + fmt.Sprint(number))
						} else if number <= 30 {
							client.send("I: " + fmt.Sprint(number))
						} else if number <= 45 {
							client.send("N: " + fmt.Sprint(number))
						} else if number <= 60 {
							client.send("G: " + fmt.Sprint(number))
						} else {
							client.send("O: " + fmt.Sprint(number))
						}
						time.Sleep(time.Duration(config.Duration) * time.Millisecond)
					}

					client.b = bingo.NewBingo()
					client.state = "init"
					client.send("Please reset the game with 'reset'")
				}()

			} else {
				client.send("Please request the board first with 'get board'")
			}

		case "mark":
			if client.state == "board-sent" {
				if client.currentNumber == 0 {
					client.send("Center number cannot be marked")
				} else {
					if client.b.MarkNumber(client.currentNumber) {
						client.send("Number marked")
						board := client.b.PrintBingoBoard()

						if config.Debug {
							fmt.Println(board)
						}

					} else {
						client.state = "init"
						client.b = bingo.NewBingo()
						client.send("Number not found on the board, Game over!")
					}
				}

				if client.b.CheckBingo() {
					client.send("Bingo! Your Flag: " + config.Flag)
					client.state = "init"

				}
			} else {
				client.send("Please request the board with 'get board' and start the game with 'play'")
			}

		case "help":
			client.send("Commands: 'get board', 'reset', 'play', 'mark', 'help'")
			client.send("get board: request the bingo board")
			client.send("reset: reset the game")
			client.send("play: start the game")
			client.send("mark: mark the sent number on the board")
			client.send("help: list of commands")

		default:
			client.send("Invalid message try 'help' for a list of commands")
		}

	}

}

func (c *client) send(v string) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	return c.conn.WriteMessage(1, []byte(v))
}
