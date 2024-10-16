FROM ubuntu:22.04
RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get -y install socat
RUN useradd -m pwn
WORKDIR /home/pwn
COPY challenge_files/look-up .
RUN echo 'UCTF{Pl4ying_S3t@r_str1ngs_is_Fun_4Evr!}' > /home/pwn/flag.txt
RUN chown -R root:root /home/pwn
RUN chmod -R 555 /home/pwn
CMD ["socat", "TCP-LISTEN:5000,reuseaddr,fork", "EXEC:'timeout 60 su pwn -c ./look-up'"]
