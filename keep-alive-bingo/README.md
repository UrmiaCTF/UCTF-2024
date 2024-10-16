# Keep Alive Bingo

<img src="Resources/Bingo.png" title="Bingo" alt="Bingo" data-align="center">

**The game is on. But this isn’t your typical Bingo. Beyond the numbers, hidden signals await your discovery. Can you keep up, or will the game leave you behind?**  
Start your investigation [here](https://keep-alive.uctf.ir/).

# Write Up

The challenge was to interact with a WebSocket server that simulates a game of Bingo. The server sends a board, and the client needs to mark the numbers as they are called out. The game ends when the server sends the message "Bingo!".

The solution is written in Go.

```golang
package main

import (
	"fmt"
	"log"
	"net/url"
	"strings"

	"github.com/gorilla/websocket"
)

func main() {

	u := url.URL{Scheme: "wss", Host: "challenge-url.com", Path: "/play"}
	log.Printf("connecting to %s", u.String())

	c, _, err := websocket.DefaultDialer.Dial(u.String(), nil)
	if err != nil {
		log.Fatal("dial:", err)
	}
	defer c.Close()

	// Receive message from server
	_, message, err := c.ReadMessage()
	if err != nil {
		log.Println("read:", err)
		return
	}
	log.Printf("received: %s", message)

	// Send message to server
	err = c.WriteMessage(websocket.TextMessage, []byte("get board"))
	if err != nil {
		log.Println("write:", err)
		return
	}

	// Receive message from server
	_, message, err = c.ReadMessage()
	if err != nil {
		log.Println("read:", err)
		return
	}

	fmt.Printf("received:\n%s", message)

	// Split the message by newline to get each line
	lines := strings.Split(string(message), "\n")

	// Initialize the map to store int-bool pairs
	intBoolMap := make(map[int]bool)

	// Iterate over each line except the first (header)
	for _, line := range lines[1:] {
		// Split the line by tab to get individual numbers
		numbers := strings.Split(line, "\t")
		// Iterate over each number
		for _, numStr := range numbers {
			// Try to convert the number string to int
			var num int
			_, err := fmt.Sscanf(numStr, "%d", &num)
			if err == nil {
				// If conversion is successful, add to map with value true
				intBoolMap[num] = true
			}
		}
	}

	// Print the resulting map
	fmt.Println(intBoolMap)

	fmt.Println(len(intBoolMap))

	// Send message to server
	err = c.WriteMessage(websocket.TextMessage, []byte("play"))
	if err != nil {
		log.Println("write:", err)
		return
	}

	// Receive message from server
	_, message, err = c.ReadMessage()
	if err != nil {
		log.Println("read:", err)
		return
	}

	fmt.Printf("received: %s\n", message)

	for {
		// Receive message from server
		_, message, err = c.ReadMessage()
		if err != nil {
			log.Println("read:", err)
			return
		}

		fmt.Printf("received: %s\n", message)

		if string(message) == "Bingo!" {
			break
		}

		if string(message[:3]) != "B: " && string(message[:3]) != "I: " && string(message[:3]) != "N: " && string(message[:3]) != "G: " && string(message[:3]) != "O: " {
			continue
		}

		// Extract the number
		numberStr := string(message[3:])

		fmt.Println("current number is: ", numberStr)

		// Try to convert the number string to int
		var number int
		_, err := fmt.Sscanf(numberStr, "%d", &number)
		if err != nil {
			fmt.Println("Error converting number to int")
			continue
		}

		if _, ok := intBoolMap[number]; ok {
			// Send message to server
			err = c.WriteMessage(websocket.TextMessage, []byte("mark"))
			if err != nil {
				log.Println("write:", err)
				return
			}

			fmt.Println("Number found in map")
		} else {
			fmt.Println("Number not found in map")
		}
	}

}
```

# Flag

```
 UCTF{P1nk_Lak3_Maharl00}
```

# Categories

- [X] Web
- [ ] Reverse
- [ ] PWN
- [ ] Misc
- [ ] Forensics
- [ ] Cryptography
- [ ] Steganography

# Points

| Warm up | This Challenge  | Evil |
| ------- |:---------------:| ----:|
| 25      |  	  100		| 500  |
