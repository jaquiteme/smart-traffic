import os
import yaml
import csv
from re import match
# [DEFAULT]
AllowedExtension = ".csv"
# [VARS]
CURR_DIR = os.path.dirname(os.path.realpath(__file__))
conf = {}
conf['car'] = None
conf['scenarios'] = None

# [FUNCTIONS]


def getEventFromInt(id):
    if (int(id) == 1):
        return '/zone/in'
    elif(int(id) == 2):
        return '/zone/out'
    else:
        return '/CAM'


def createMovementObj(event, perform, speed, heading, position):
    movement = {}
    movement['event'] = event
    movement['perform'] = perform
    movement['speed'] = speed
    movement['heading'] = heading
    movement['position'] = position

    return movement


def newScenario():
    requestMatch = "[o,oui]"
    doUserContinue = input(
        'Souhaitez-vous créer un autre scénario ? O(oui)/N(non) :')
    if(match(requestMatch, doUserContinue)):
        return True
    else:
        return False


def readCoordinatesFile(filename):
    content = []
    if(filename != None):
        try:
            with open('{}/{}.csv'.format(CURR_DIR, filename), encoding='UTF-8') as coordFile:
                data = csv.DictReader(coordFile)
                for row in data:
                    content.append(row)
                coordFile.close
                return content
        except ValueError as ex:
            print("Message d'erreur: {}".format(ex))


def generateConfFile(conf):
    filename = '{}/{}.config.yml'.format(CURR_DIR, conf['car']['stationId'])
    try:
        with open(filename, 'w+') as yamlFile:
            yaml.dump(conf, yamlFile, sort_keys=False)
        print("File successfully created !")
    except:
        print("Error when creating file")


def setDenmEventFromCauseCode(code):
    if (code == 4):
        return '/DENM/accident'


print('-'*50)
print('#SCRIPT DE GENERAION DU FICHIER DE CONFIG D UNE VOITURE')
print('#AUTHOR: JORDY AQUITEME')
print('-'*50)
print('Avant de commencer, copier le fichier contenant les coordonnées dans le répertoire')
print('Extension autorisées: [{}]'.format(AllowedExtension))

filename = input("Nom du fichier: ")
# CAR INFOS
print('-'*30)
print('DEFINITION DE LA VOITURE')
print('-'*30)
car = {}
print('# [sationId] : identifiant de la voiture')
car['stationId'] = input('STATION ID: ')
print('# [stationType] : \n -> véhicule ordinaire (5) \n -> véhicule d’urgence (10) \n -> véhicule opérateurRoutier (15)')
car['stationType'] = int(input('SATION TYPE: '))
#
content = readCoordinatesFile(filename)
print(content)
# hop
step = 1
# initialtion du tableau des scenarii
scenarios = []
# pour la premiere execution
_continue = True
while _continue:
    print('-'*30)
    print('DEFINITION DES SCENARII')
    print('-'*30)
    print('Créer votre scénario :')
    scenario = {}
    scenario['name'] = input('Donner un nom au scénario : ')
    scenario['movements'] = []
    print('-'*30)
    for row in content:
        print('-'*10, '[ETAPE {}]'.format(step), '-'*10)
        print('#L evenement à simuler :')
        print('#Entrer dans une zone -> (1)')
        print('#Sortie d une zone -> (2)')
        print('#Normal (3)')
        event = ''
        # Fichier
        if row['event'] != '':
            event = int(row['event'])
        else:
            event = input('Evenement (par défaut (3)) :')
        if (event == ''):
            event = getEventFromInt(3)
        else:
            event = getEventFromInt(event)
        # DENM
        requestMatch = "[o,oui]"
        doUserPerformDenm = ''
        if (row['perform'] != ''):
            doUserPerformDenm = row['perform']
        else:
            doUserPerformDenm = input(
                'Souhaitez-vous simuler un message DENM à cette étape ? O(oui)/N(non) :')
        if(match(requestMatch, doUserPerformDenm)):
            perform = {}
            causeCode = 0
            if (row['causeCode'] != ''):
                causeCode = int(row['causeCode'])
            else:
                causeCode = int(input('Cause code : '))
            perform['causeCode'] = causeCode
            perform['subCauseCode'] = None
            perform['event'] = setDenmEventFromCauseCode(causeCode)

        else:
            perform = None
        # VITESSE
        speed = 0
        if (row['speed'] != ''):
            speed = float(row['speed'])
        else:
            speed = float(input('Vitesse : '))
        heading = 0
        position = {}
        position['latitude'] = float(row['latitude'])
        position['longitude'] = float(row['longitude'])
        scenario['movements'].append(createMovementObj(
            event, perform, speed, heading, position))
        #
        step += 1

    scenarios.append(scenario)
    # On demande à l utilisateur s il souhaite continuer
    _continue = newScenario()

conf['car'] = car
conf['scenarios'] = scenarios
generateConfFile(conf)
