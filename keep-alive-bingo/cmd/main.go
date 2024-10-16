package main

import (
	"KeepAliveBingo/internal/config"
	"KeepAliveBingo/internal/handlers"
	"log"
	"net/http"
)

func main() {
	config, err := config.InitConfig()
	if err != nil {
		log.Fatalf("failed to init config: %v", err)
	}

	go handlers.ListenToWSChannel(*config)

	http.HandleFunc("/", handlers.Home)
	http.HandleFunc("/play", handlers.Play)

	log.Fatal(http.ListenAndServe("0.0.0.0:"+config.Port, nil))

}
