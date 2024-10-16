package main

import (
	"flag"
	"fmt"
	"os"

	"github.com/spf13/viper"
)

var configPath string

func init() {
	flag.StringVar(&configPath, "config", "", "Android Emulator Serverless config file path")
}

type Config struct {
	ListenPort string `mapstructure:"listenPort"`
	ADBAddress string `mapstructure:"adbAddress"`
}

func InitConfig() (*Config, error) {
	if configPath == "" {
		configPathFromEnv := os.Getenv("CONFIG_PATH")
		if configPathFromEnv != "" {
			configPath = configPathFromEnv
		} else {
			getwd, err := os.Getwd()
			if err != nil {
				return nil, fmt.Errorf("failed to get working directory: %v", err)
			}
			configPath = fmt.Sprintf("%s/config.yaml", getwd)
		}
	}

	cfg := &Config{}

	viper.SetConfigType("yaml")
	viper.SetConfigFile(configPath)

	if err := viper.ReadInConfig(); err != nil {
		return nil, fmt.Errorf("failed to read config file: %v", err)
	}

	if err := viper.Unmarshal(cfg); err != nil {
		return nil, fmt.Errorf("failed to unmarshal config file: %v", err)
	}

	return cfg, nil
}
