FROM debian:bookworm-slim

RUN apt-get update && apt-get install -y socat

# Create a directory for the challenge
WORKDIR /home/ctf

COPY pwn_hard .

RUN echo "UCTF{p3rs1an_c4t}" > flag.txt

# Make sure the binary is owned by root and has appropriate permissions
RUN chown root:root pwn_hard \
    && chmod 4755 pwn_hard \
    && chmod 444 flag.txt

EXPOSE 1337

# Run the challenge using socat to listen on port 1337
CMD ["socat", "TCP-LISTEN:1337,reuseaddr,fork", "EXEC:/home/ctf/pwn_hard"]
