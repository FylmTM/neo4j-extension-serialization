# Neo4j Extension - Kryo

## Setup
Build:
```shell
$ mvn clean install
```

Install:
```
# neo4j-server.properties
org.neo4j.server.thirdparty_jaxrs_classes=me.vrublevsky.neo4j.extension.serialization.kryo.rest=/unmanaged/serialization/kryo
```
