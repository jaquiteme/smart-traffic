#!/bin/sh
exec mvn -file  /projects/xmpp_client exec:java -Dexec.mainClass="com.jordy.backup.App"
