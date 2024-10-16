package main

import (
	"fmt"
	"time"
	"os"

	MQTT "github.com/eclipse/paho.mqtt.golang"
)

const (
	BROKER    = "tcp://188.121.96.240:1883"
	TOPIC_SUB = "newpath"
	TOPIC_PUB = "new/path"
)

var counter = 0


func onConnect(client MQTT.Client) {
	fmt.Println("Connected with result code 0")
	if token := client.Subscribe(TOPIC_SUB, 0, nil); token.Wait() && token.Error() != nil {
		fmt.Println(token.Error())
		os.Exit(1)
	}
}

func connect(client MQTT.Client, opts * MQTT.ClientOptions) {
	for {
		if token := client.Connect(); token.Wait() && token.Error() != nil {
			fmt.Printf("Connection failed: %s\n", token.Error())
			time.Sleep(10 * time.Second)  
			continue
		}
		break
	}
}

func main() {
	opts := MQTT.NewClientOptions().AddBroker(BROKER)
	opts.SetClientID("go_mqtt_client")
	opts.SetKeepAlive(60 * time.Second)     
	opts.SetAutoReconnect(true)            
	opts.SetMaxReconnectInterval(10 * time.Second)  
	opts.OnConnect = onConnect

	client := MQTT.NewClient(opts)
	connect(client, opts)

	go func() {
		for {
			message := "can you hear me!"
			token := client.Publish(TOPIC_PUB, 0, false, message)
			token.Wait()

			fmt.Printf("Published message: %s\n", message)
			time.Sleep(5 * time.Second)
			counter++
			if counter == 15 {
				client.Disconnect(250)
				fmt.Println("Exiting...")
				os.Exit(0)
			}
		}
	}()

	select {}
}
