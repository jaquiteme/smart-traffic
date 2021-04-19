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

1. ### Cloner le projet

```git clone https://github.com/jordy-aquiteme/smart-traffic```

2. ### Domaine 

Ajouter les lignes suivantes dans votre fichier `/etc/hosts`:
* 10.5.0.2 example.com
* 10.5.0.2 pubsub.example.com

3. ### Volumes

Créé un volume pour mongoDB en exécutant la commande suivante: 

```docker volume create mongo-db```

:warning: Assurer vous d'être connecté à internet.

## Usage
[TODO]