import os
import paho.mqtt.client as paho

broker="127.0.0.1"
port = 1883
stationId = os.environ.get('STATION_ID')
stationType = os.environ.get('STATION_TYPE')

def on_publish(client, userdata, result):            
    print("data published \n")

client1 = paho.Client(stationId)                          
client1.on_publish = on_publish                          
client1.connect(broker, port)                                 
ret= client1.publish("house/bulb1","on")  