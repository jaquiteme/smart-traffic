#!/bin/sh

# exec tail -f /dev/null
# exec keytool -importcert -alias prosody.server -cacerts -file /projects/crt/example.com.crt -storepass changeit -noprompt
# exec keytool -import -alias prosody.server.pubsub -cacerts -file /projects/crt/pubsub.example.com.crt -storepass changeit -noprompt
exec mvn -file  /projects/road_gw exec:java -Dexec.mainClass="com.jordy.gateway.app.App"
