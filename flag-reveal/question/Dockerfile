# Use an official Python image as a parent image
FROM python:3.9-slim-bullseye

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl \
    git \
    socat \
    && rm -rf /var/lib/apt/lists/*

# Install Foundry toolset
RUN curl -L https://foundry.paradigm.xyz | bash && \
    bash -c "source ~/.bashrc && foundryup"

# Create directories
RUN mkdir -p /challenge/contracts
COPY ./challenge/contracts /challenge/contracts

# Set the working directory
WORKDIR /challenge/contracts

# Compile the smart contract
RUN bash -c "source ~/.bashrc && forge build"

# Add deployment scripts and start-up configuration
COPY ./config/entrypoint.sh /
COPY ./config/start-ctf.sh /challenge/

RUN chmod +x /entrypoint.sh /challenge/start-ctf.sh

ENTRYPOINT ["/entrypoint.sh"]
