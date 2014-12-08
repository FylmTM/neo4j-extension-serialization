#!/bin/bash

EXTENSION_ROOT=$HOME/workspace/neo4j-extension-kryo
DATABASE_ROOT=$HOME/tools/neo4j-community-2.2.0-M01

#cd $EXTENSION_ROOT && mvn clean install
#echo "---> Deploy in 3 seconds"
#sleep 3


echo "---> Stop database"
cd $DATABASE_ROOT && bin/neo4j stop


echo "---> Copy extensions"
cp \
    $EXTENSION_ROOT/neo4j-extension-serialization-kryo/target/neo4j-extension-serialization-kryo-1.0-SNAPSHOT-jar-with-dependencies.jar \
    $DATABASE_ROOT/plugins/

cp \
    $EXTENSION_ROOT/neo4j-extension-serialization-bson/target/neo4j-extension-serialization-bson-1.0-SNAPSHOT-jar-with-dependencies.jar \
    $DATABASE_ROOT/plugins/


echo "---> Start database"
cd $DATABASE_ROOT && bin/neo4j start


