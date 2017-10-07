## Init

Project created by spring CLI command below, and edited in Android studio.

```
springboot init --build=gradle --dependencies=web,data-redis throttler
```

## Run
```
gradle build
```

```
gradle bootRun    # or
java -jar build/libs/throttler-0.0.1-SNAPSHOT.jar
```