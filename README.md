# Spring TodoList Backend

## Local Development

Clone the repository

```shell
git clone https://github.com/pickled-dict/spring-todolist-backend/
```

## Docker (highly recommended)

If Docker is installed simply run the `startup.sh` script -- this will build the required `.jar` file with Gradle -- then run `docker-compose`

```
./startup.sh
```

Both containers will launch in detached mode via `docker-compose up -d`.

To stop the running containers you can run the `./shutdown.sh` script or use the command `docker-compose down`.

The Spring Boot application will be running at `http://localhost:8080/`

## Gradle

The pre-requisite for this is that a PostreSQL database needs to be running. The default configuration for local development has a database called 
`spring_todo`, a user called `user` with a password of `password`

If you'd like, you can customize `src/main/resources/application.yml` to point to a custom PostgreSQL database of your choice:

```
spring:
  datasource:
    url: "jdbc:postgresql://<DATABASE_URL>:5432/<DATABASE>"
    username: <USER>
    password: <PASSWORD>
```

Once the database is configured/online, run

```
./gradlew bootRun
```

which will launch the Spring Boot application via Gradle at `http://localhost:8080`
