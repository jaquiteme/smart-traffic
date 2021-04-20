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

Ajouter les lignes suivantes dans votre fichier `/etc/hosts`:
* 10.5.0.2 example.com
* 10.5.0.2 pubsub.example.com

```docker volume create mongo-db```

:warning: Assuré vous d'être connecté à Internet.

3. ### **Intaller Docker Compose**

https://docs.docker.com/compose/install/

## Usage

[TODO]

1. ### **Démarrage**

* Serveurs et passerelles

Dans une console, taper la commande ci-dessous :

  ```docker-compose -f smart-traffic/docker-compose.yml up```

:warning: Le démarrage peut prendre quelques minutes et il faudrait attendre avant de démarrer la simulation !!!

* Simulation

Dans une autre console, taper la commande ci-dessous :

```docker-compose -f smart-traffic/car/docker-compose.yml up```