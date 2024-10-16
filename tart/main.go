package main

import (
	"bytes"
	"io"
	"log"
	"net"
	"strings"

	"golang.org/x/text/encoding/unicode"
	"golang.org/x/text/transform"
)

var config *Config

func main() {
	var err error

	config, err = InitConfig()
	if err != nil {
		log.Fatalf("failed to init config: %v", err)
	}

	listener, err := net.Listen("tcp", ":"+config.ListenPort)
	if err != nil {
		log.Fatal(err)
	}

	for {
		srcConn, err := listener.Accept()
		if err != nil {
			log.Fatal(err)
		}

		go handle(srcConn)
	}
}

func handle(srcConn net.Conn) {
	dstConn, err := net.Dial("tcp", config.ADBAddress)
	if err != nil {
		log.Fatal(err)
	}

	go copyConnSD(srcConn, dstConn)
	go copyConnDS(dstConn, srcConn)
}

func copyConnSD(writer, reader net.Conn) {
	defer writer.Close()
	defer reader.Close()

	buf := make([]byte, 2048)
	for {
		n, err := reader.Read(buf)
		if err != nil {
			if err != io.EOF {
				log.Fatal(err)
			}
			break
		}

		_, err = writer.Write(buf[:n])
		if err != nil {
			log.Fatal(err)
		}
	}
}

func copyConnDS(writer, reader net.Conn) {
	defer writer.Close()
	defer reader.Close()

	decoder := unicode.UTF8.NewDecoder()

	buf := make([]byte, 2048)
	var dataBuffer bytes.Buffer

	for {
		n, err := reader.Read(buf)
		if err != nil {
			if err != io.EOF {
				log.Fatal(err)
			}
			break
		}

		dataBuffer.Write(buf[:n])

		data, _, err := transform.String(decoder, dataBuffer.String())
		if err != nil {
			log.Fatal(err)
		}

		if checkData(data) {
			_, err = writer.Write(dataBuffer.Bytes())
			if err != nil {
				log.Fatal(err)
			}
			log.Printf("Received data DS: %s", data)
		} else {
			_, err = writer.Write([]byte("echo \"You are not allowed to send this data\" \n\r"))
			if err != nil {
				log.Fatal(err)
			}
			log.Printf("Blocked data DS: %s", data)
		}
		dataBuffer.Reset()
	}
}

func checkData(data string) bool {
	// Check for allowed substrings
	if strings.Contains(data, "features") && strings.Contains(data, "cmd") {
		return true
	}
	// Check for disallowed substrings
	if strings.Contains(data, "OPE") && strings.Contains(data, "shell") && strings.Contains(data, "pty") {
		return false
	}
	// check for uninstallation
	if strings.Contains(data, "uninstall") {
		return false
	}
	// Default to not sending if conditions are not met
	return true
}
