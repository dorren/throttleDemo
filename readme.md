## Init

Project created by spring CLI command below, and edited in Android studio.

```
springboot init --build=gradle --dependencies=web,data-redis throttleDemo
```

## Run
```
gradle build
```

```
gradle bootRun    # or
java -jar build/libs/throttler-0.0.1-SNAPSHOT.jar
```

## Config

change src/.../ThrottlingConfig.MAX_REQ_LIMIT value for max limit.

## Dev tools

* redis-commander