# Game Of Three

This project is a sample number game for only two player (player per an app instance). The demostarate how should implement in a business architecture some part seems to be over engineering. It's a necessary while implementing an enterprise application with large team to keep project clean and maintable.       

## Libraries&Tools
* Java 11
* Gradle
* Spring Boot 2
  - Spring WebFlux
  - Spring Data Redis
  - Spring Data RabbitMQ
  - Actuator
* Lombok
* JMap
* Redis
* RabbitMQ
* Docker/ Docker compose

## How to run

##### Using make file 

Move to project root directory and execute ```make```

##### Using docker-compose 
Move to project root directory and execute ```./gradlew build```

After successfully build execute ```docker-compose up --build```

Please do not run with docker compose in silent mode ```docker-compose up -d``` because the system **only interacts** with **terminal logs**  

## How to play 
After successfully run the project. Please send the following request to player-1;

```curl -H "Content-Type: application/json" -X POST -d '{"number":100}' http://localhost:8081/game/play```

Second player needs to human interaction to continue unless it's changed _AUTOMATIC_ mode. When second player turns, please send following request to player-2
 
```curl -H "Content-Type: application/json" -X PUT -d '{"id":"theIdFromLog"}' http://localhost:8082/game/play```


#####Exceptions
When it's not player turn, called game/play endpoint system will give following exception;
```json
{
    "timestamp": "2020-10-12T12:34:58.640+00:00",
    "path": "/game/play",
    "status": 425,
    "error": "Too Early",
    "message": "",
    "requestId": "71c7a40b-2"
}
```

When user tries to play  move which doesn't exists in the system, system will give following exception;
```json
{
    "timestamp": "2020-10-12T12:51:43.352+00:00",
    "path": "/game/play",
    "status": 404,
    "error": "Not Found",
    "message": "",
    "requestId": "71c7a40b-4"
}
```

# Docker

The project contains a docker-compose files:

docker-compose.yml - that runs **two** application instance (player-1 and player-2) with related services (redis, rabbitMQ)

_player-1_ is automatic player 

_player-2_ is manual player 

Please check the `Environment Variables` Section to see how to configure app instances.

## How to import project in Intellij IDEA
#### Open project in Intellij IDEA
Open -> choose project directory -> Open

Check options:

* Use auto-import

* Create separate module per source set

* Use default gradle wrapper

#### Needed plugins

* Lombok

#### Set annotation processing
Open Settings/Preferences -> Build, Execution, Deployment -> Compiler -> Annotation Processors

Check options:

* Enable annotation processing

* Obtain processors from project class path

* Module content root

#### Enable Lombok
1. Open Settings/Preferences -> Other settings -> Lombok plugin
2. Check option: Enable Lombok plugin for this project

## Health check

The service provides health check at `GET /internal/health`.
Example response is as follows:

```json
{
  "status": "UP",
  "details": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 502468108288,
        "free": 422851829760,
        "threshold": 10485760
      }
    },
    "refreshScope": {
      "status": "UP"
    }
  }
}
```

## Environment

### Environment variables
|Name|Default|Description
|---|---|---|
|APP_NAME|player-1|Name of the application (to be referred in logs and other introspective tools)|
|APP_PORT|8080|Main application port|
|MANAGEMENT_PORT|8090|Application port for management endpoints|
|NEXT_PLAYER_BASE_URL|http://localhost:8080|Next player base url|
|CONNECTION_TIME_OUT_IN_MS|1000|Webflux client connection time out in seconds
|READ_TIME_OUT_IN_SEC|10|Webflux client connection read time out in milliseconds|
|WRITE_TIME_OUT_IN_SEC|10|Webflux client connection write time out in milliseconds|
|NEXT_PLAYER_URL|/game/play|Next player play end point url|
|NEXT_PLAYER_HTTP_METHOD|POST|Next player play end point http method|
|END_GAME_URL|/game/endgame|Next player end game end point|
|END_GAME_HTTP_METHOD|DELETE|Next player end game end http method|
|PLAY_MODE|AUTOMATIC|Player mode available values AUTOMATIC or MANUAL
|RABBIT_HOST|localhost|RabbitMq Host|
|RABBIT_PORT|5672|RabbitMq Port|
|RABBIT_USER|gameUser|RabbitMq User|
|RABBIT_PASSWORD|gameUserPassword|RabbitMq password|
|RABBIT_EXHANGE|gameExgange|RabbitMq exhange|
|RABBIT_ROUTING_KEY|gameRoutingKey|RabbitMq Routing Key|
|RABBIT_QUEUE|gameQueue|RabbitMq queue name|
|REDIS_HOST|localhost|Redis host|
|REDIS_PORT|6379|Redis port|