# Neo4j Extension - Kryo

## Setup
1) Build:
```shell
$ mvn clean install
```

2) Grab extension from `neo4j-extension-kryo-core/target`
3) Put that extension into database `plugins` directory.
4) Setup extension:
```
# neo4j-server.properties
org.neo4j.server.thirdparty_jaxrs_classes=me.vrublevsky.neo4j.extension.kryo.rest=/unmanaged/db
```
5) Run server!
