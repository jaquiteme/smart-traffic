import os
import sys
import getopt
import time
import yaml
import paho.mqtt.client as mqtt
import time
import json
from Struct import Struct

CURR_DIR = os.path.dirname(os.path.realpath(__file__))
#
topicParent = "cars/data"
# configFileName = ""
# configFile = '{}/{}'.format(CURR_DIR, configFileName)

# with open('{}'.format(configFile)) as file:
#     data = yaml.safe_load(file)

# config = None
# structure de données de la voiture
# car = None
#
connected = False

def loadConfigFile(argv):
    #temps d'attente avant démarrage de la simulation
    start_in = int(os.environ.get('WAITING'))
    if start_in > 0:
        print(f"Ce simulateur attendra {start_in}s avant de démarrer.")
        time.sleep(start_in)
    try:
        opts, args = getopt.getopt(argv, "hi:o", ["ifile="])
    except getopt.GetoptError:
        sys.exit(2)
    for opt, arg in opts:
        if (opt == '-i'):
            configFile = '{}/{}'.format(CURR_DIR, arg)
            with open('{}'.format(configFile)) as file:
                data = yaml.safe_load(file)
                config = Struct(**data)
                car = Struct(**config.car)
            return config, car
             


def on_connect(client, userdata, flags, rc):
    if rc == 0:
        client.connected_flag = True
        connected = True
        print(" client.connected_flag ", client.connected_flag)
    else:
        print("Bad connection Returned code=", rc)


def getConnection(car):
    # MQTT CONNECTION
    retry = 10
    # broker config
    broker = "10.5.0.8"
    brokerPort = 1883
    # creation d une nouvelle instance
    client = mqtt.Client(car.stationId, clean_session=True)
    client.on_connect = on_connect  # call back function
    try:
        print("Connexion au broker ", broker)
        client.connect(broker, brokerPort)
    except:
        print("Echec de connexion au broker ", broker)
        time.sleep(5)

    return client


def generateCamMessage(car, step):
    message = {}
    message['stationId'] = car.stationId
    message['stationType'] = car.stationType
    message['speed'] = step.speed
    message['heading'] = step.heading
    message['positions'] = step.position
    return message


def generateDenmMessage(car, step):
    message = {}
    position = {}
    message['stationId'] = car.stationId
    message['stationType'] = car.stationType
    message['causeCode'] = step.perform['causeCode']
    message['subCauseCode'] = step.perform['subCauseCode']
    message['positions'] = step.position
    return message


def publishCamMessage(client, step, car):
    topic = '{}{}'.format(topicParent, step.event)
    payload = generateCamMessage(car, step)
    response = client.publish(
        '{}'.format(topic),  json.dumps(payload), 0)
    status = response[0]
    return status, topic, payload


def publishDenmMessage(client, step, car):
    topic = '{}{}'.format(topicParent, step.perform['event'])
    payload = generateDenmMessage(car, step)
    response = client.publish(
        '{}'.format(topic),  json.dumps(payload), 0)
    status = response[0]
    return status, topic, payload


def run(argv):
    config, car = loadConfigFile(argv)
    if car != None:
        client = getConnection(car)
        client.loop_start()

    # interval de temps pour l envoie des messages
    interval = 1  # (par défaut)
    try:
        for scenario in config.scenarios:
            for mov in scenario['movements']:
                step = Struct(**mov)
                # simulation d un message DENM
                if (step.perform != None):
                    # Envoyer un message DENM
                    status, topic, payload = publishDenmMessage(client, step, car)
                    if status == 0:
                        print(f"Send `{payload}` to topic `{topic}`")
                    else:
                        print(f"Failed to send message to topic {topic}")
                # Envoyer un message CAM
                status, topic, payload = publishCamMessage(client, step, car)
                if status == 0:
                    print(f"Send `{payload}` to topic `{topic}`")
                else:
                    print(f"Failed to send message to topic {topic}")
                # Fréquence
                if (payload['speed'] >= 90):
                    interval = 0.1  # 10 HZ (100ms)
                else:
                    interval = 1  # 1 HZ (1s)
                time.sleep(interval)
    except KeyboardInterrupt:
        client.loop_stop()  # Stop loop
        client.disconnect()  # déconnexion


if __name__ == '__main__':
    run(sys.argv[1:])
