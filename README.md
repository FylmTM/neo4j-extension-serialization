# Neo4j Extension Serialization

This is proof-of-concept, to show how custom serialization can be implemented in Neo4j.

Why? Because it's fast.

Let's take movie graph, that Neo4j offers for fresh database installation and do some tests and import it 5 times (so we have a bit more data to see difference).


Type               | Request time
------------------ | -------------
**Default, JSON**  | ~130ms
**Custom, Kryo**   | ~30ms
**Custom, BSON**   | ~30ms

Note: You can find all test shell scripts and query in `bin` directory.

## Setup
Build:
```shell
$ mvn clean install
```

Install:
```
# neo4j-server.properties

# For Kryo
org.neo4j.server.thirdparty_jaxrs_classes=me.vrublevsky.neo4j.extension.serialization.kryo.rest=/unmanaged/kryo
# For BSON
org.neo4j.server.thirdparty_jaxrs_classes=me.vrublevsky.neo4j.extension.serialization.bson.rest=/unmanaged/bson
```
