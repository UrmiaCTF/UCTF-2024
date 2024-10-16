import paho.mqtt.client as mqtt


BROKER = '37.152.182.93'
PORT = 1883
TOPIC_SUB = 'new/path'
TOPIC_PUB = 'newpath'

def on_connect(client, userdata, flags, rc):
    print(f"Connected with result code {rc}")
    client.subscribe(TOPIC_SUB)
    

def on_message(client, userdata, msg):
    print(f"Received message: {msg.payload.decode()} on topic: {msg.topic}")
    response = "NEW TOPIC FOR FLAG IS uctf/flag"
    client.publish(TOPIC_PUB, response)

client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect(BROKER, PORT, 60)

client.loop_forever()

