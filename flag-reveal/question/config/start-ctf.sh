#!/bin/bash

FLAG=554354467b5734726d55705f6e305f707231766174455f443474347d00000000

# Start a local Ethereum node with Foundry
anvil --chain-id 1337 --host 0.0.0.0 &

sleep 2 # Wait for Anvil to start

# Set the path to the contract and the private key (replace with your actual private key)
CONTRACT_PATH="./FlagReveal.sol:FlagReveal"
PRIVATE_KEY="0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80"

# Deploy the contract using Foundry’s `forge` command
CONTRACT_ADDRESS=$(forge create --rpc-url http://localhost:8545 --private-key $PRIVATE_KEY $CONTRACT_PATH --constructor-args $FLAG)

echo "Contract deployed at: $CONTRACT_ADDRESS"

# Keep the container running
tail -f /dev/null
