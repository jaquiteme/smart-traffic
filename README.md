# SMART TRAFFIC

## Description 
Le contexte de ce projet se situe dans la découverte et l'utilisation des protocoles de communication à faible coût suivant : 
* XMPP
* MQTT 
qui jouent une grande importance dans le monde l'**IoT**.

## Architecture du projet

![](images/archi_smart_traffic.png?raw=true)

## Fonctionement
[TODO]
![](images/fonctionement_smart_traffic.jpg?raw=true)

## Installation

1. ### **Clone du projet**

```git clone https://github.com/jordy-aquiteme/smart-traffic```

2. ### **Nom de Domaine**

Ajoutez les lignes suivantes dans votre fichier `/etc/hosts`:
* 10.5.0.2 example.com
* 10.5.0.2 pubsub.example.com

:warning: Assurez vous d'être connecté à Internet.

3. ### **Installation de Docker Compose**

https://docs.docker.com/compose/install/

## Usage

[TODO]

1. ### **Démarrage**

* Serveurs et passerelles

Dans une console, tapez la commande ci-dessous :

  ```docker-compose -f smart-traffic/docker-compose.yml up```

:warning: Le démarrage peut prendre quelques minutes et il faudrait attendre avant de démarrer la simulation !!!

* Simulation

Dans une autre console, tapez la commande ci-dessous :

```docker-compose -f smart-traffic/car/docker-compose.yml up```

Le simulateur d'une voiture est construit comme suit :

- Un fichier de configuration (sous format ```.yml```), pour simuler les mouvements de la voiture.

```yaml 
car:
  stationId: FR-CAR-1
  stationType: 5
scenarios:
- name: normal
  movements:
  - event: /zone/in
    perform: null
    speed: 90.0
    heading: 0
    position:
      latitude: 49.282851
      longitude: 4.107277
```
Ce fichier peut être construit de deux manière :

* 1ère manière :

Préparez un fichier ```.csv```(délimité par des virgules) des coordonnées comme suit :

![](car/data/code/CarConfigGenerator.py?raw=true)

Et exécuter le script ```mart-traffic/car/data/code/CarConfigGenerator.py``` et se laissé guidé. 

* 2ème manière :

En fournissant un fichier toujours sous format ```.csv``` (délimité par des virgules).



- Un programme d'exécution.

PS: les diofférents packets envoyées peuvent être visualisés dans les logs ```docker-compose``` correspondants.