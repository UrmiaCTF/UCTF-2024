FROM ubuntu:22.04
RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get -y install socat
RUN useradd -m pwn
WORKDIR /home/pwn
COPY challenge_files/orange .
RUN echo 'UCTF{G1l4n_0r4nge5_4r3_V3ry_d3l1c1ous}' > /home/pwn/flag.txt
RUN chown -R root:root /home/pwn
RUN chmod -R 555 /home/pwn
CMD ["socat", "TCP-LISTEN:5001,reuseaddr,fork", "EXEC:'timeout 60 su pwn -c ./orange'"]
