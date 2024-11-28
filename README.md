# Coop

This app was created with springboot 3.2 - tips on working with the code  

## Development

When starting the application `docker compose up` is called and the app will connect to the contained services.
[Docker](https://www.docker.com/get-started/) must be available on the current system.

During development it is recommended to use the profile `local`. In IntelliJ `-Dspring.profiles.active=local` can be
added in the VM options of the Run Configuration after enabling this property in "Modify options". Create your own
`application-local.yml` file to override settings for development.

Lombok must be supported by your IDE. For IntelliJ install the Lombok plugin and enable annotation processing -
[learn more](https://bootify.io/next-steps/spring-boot-with-lombok.html).

After starting the application it is accessible under `localhost:8080`.
## Configure Spring Datasource, JPA, App properties
1. Clone the repository
2. Open src/main/resources/application.yml
```

spring:
  rabbitmq:
    port: '5672'
    host: localhost
    username: administrator
    password: RootL3g3ndary++!
  datasource:
    url: ${JDBC_DATABASE_URL:jdbc:postgresql://localhost:5432/coop}
    username: ${JDBC_DATABASE_USERNAME:administrator}
    password: ${JDBC_DATABASE_PASSWORD:RootL3g3ndary++!}
    hikari:
      connection-timeout: 30000
      maximum-pool-size: 10
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
    properties:
      hibernate:
        jdbc:
          lob:
            non_contextual_creation: true
        id:
          new_generator_mappings: true

  mvc:
    format:
      date-time: iso

  application:
    name: coop
  docker:
    compose:
      lifecycle-management: start-only
error:
  handling:
    http-status-in-json-response: true
    exception-logging: NO_LOGGING
    full-stacktrace-http-statuses: 5xx
    log-levels:
      5xx: ERROR
    codes:
      UserUsernameUnique: USER_USERNAME_UNIQUE
      UserEmailUnique: USER_EMAIL_UNIQUE
springdoc:
  pathsToMatch: /, /api/**
server:
  servlet:
    context-path: /api
  port: '9093'


application:
  security:
    jwt:
      secret-key: 586B633834416E396D7436753879382F423F4428482B4C6250655367566B5970
      expiration: 900000 #  15 min in ms
      cookie-name: jwt-cookie
      refresh-token:
        expiration: 1296000000 # 15 days in ms
        cookie-name: refresh-jwt-cookie
logging:
  level:
    org:
      hibernate: WARN
      springframework:
        web: WARN
    root: WARN
  file:
    name: myapp.log
    max-history: '30'
    max-size: 10MB


```

## Build

The application can be built using the following command:

```
mvnw clean package
```

Start your application with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./target/coop-0.0.1-SNAPSHOT.jar
```

If required, a Docker image can be created with the Spring Boot plugin. Add `SPRING_PROFILES_ACTIVE=production` as
environment variable when running the container.

```
mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=kimotho/coop
```



The application will be available at http://localhost:9093.

# Test project
## User registration endpoint

`POST http://localhost:9093/api/v1/auth/register`

 

For detailed documentation and testing of the APIs, access the Swagger UI by visiting:
```
http://localhost:9093/swagger-ui.html
```