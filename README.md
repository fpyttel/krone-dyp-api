# krone-dyp-api

## setup

Rename */krone-dyp-api/src/main/resources/application.example.properties* to *application.properties* and configure the DB settings.


## install and run

Run as standalone backend.

```
gradlew clean build run
```

## endpoints

http://localhost:8042/api/dyp/list
http://localhost:8042/api/dyp/{dypId}
...
http://localhost:8042/api/player/list
http://localhost:8042/api/player/{playerId}
...
