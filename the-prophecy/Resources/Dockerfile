FROM python:3.9
WORKDIR /code
COPY ./requirements.txt /code/requirements.txt
RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt
COPY ./server.py /code/server.py
CMD ["fastapi", "run", "server.py", "--port", "80"]