# Dockerfile
FROM golang:1.22

# Set the Current Working Directory inside the container
WORKDIR /app

# Copy everything from the current directory to the PWD(Present Working Directory) inside the container
COPY . .

# This container exposes port 8888 to the outside world
EXPOSE 8888

# Compile the Go app
RUN go build -o main .

# Run the binary program produced by `go install`
CMD ["./main"]