import paho.mqtt.client as mqtt
BROKER = '37.152.182.93'
PORT = 1883
TOPIC_PUB = 'uctfflag'
TOPIC_SUB = 'uctf/flag'

def on_connect(client, userdata, flags, rc):
    print(f"Connected with result code {rc}")
    client.subscribe(TOPIC_SUB)


def on_message(client, userdata, msg):
    print(f"Received message: {msg.payload.decode()} on topic: {msg.topic}")
    response = "flag :  uctf{Q@!3h_Rudkh@n}"
    client.publish(TOPIC_PUB, response)

client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect(BROKER, PORT, 60)

client.loop_forever()
