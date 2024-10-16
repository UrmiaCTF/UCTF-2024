FROM docker.arvancloud.ir/python:3-alpine

RUN apk --no-cache add socat

WORKDIR /app

COPY requirements.txt  requirements.txt
COPY script.py script.py

RUN pip install --no-cache -r requirements.txt

EXPOSE 5000

CMD ["socat", "TCP-LISTEN:1001,reuseaddr,fork", "EXEC:'timeout 60 ./script.py'"]
