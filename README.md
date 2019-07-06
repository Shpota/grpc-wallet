[![Build Status](https://travis-ci.com/Shpota/grpc-wallet.svg?branch=master)](https://travis-ci.com/Shpota/grpc-wallet)

# Wallet gRPC API implementation
A simple wallet gRPC API implementation with Spring Boot and MySQL.

## Build and run
### Preconditions:
You need to have Docker installed to be able to build and run the
application.

### Please perform the following steps:
##### Build an application image
```sh
sudo docker build -t=wallet .
```
##### Create a docker network
```sh
sudo docker network create wallet-net
```
##### Start the DB
```
sudo docker run --name wallet-db \
  -e MYSQL_DATABASE=wallet \
  -e MYSQL_USER=wallet \
  -e MYSQL_PASSWORD=wallet \
  -e MYSQL_ROOT_PASSWORD=wallet \
  --net=wallet-net \
  mysql:5.7.26 
```
##### After the DB is running, start the application
```sh
sudo docker run --name=wallet-app \
  --net=wallet-net \
  -p 6565:6565 \
  wallet:latest
```

After the application has started the API is accessible
on port 6565 of your local machine. Check [the API definition](src/main/proto/wallet.proto)
to be able to perform gRPC requests.

*Note:* I tested the build only on a Linux machine. The commands should also work on Mac
(`sudo` is not required).

## Technologies and solutions I used
I used the technology stack declared in the specification, in particular:  
- Java 8+ (I used 11)
- Spring 5.x
- gRPC
- MySQL 5.x
- Gradle
- JUnit
- Docker
- Hibernate
- Spring Boot 2.x

Apart from the required technologies, I took 
[LogNet gRPC Spring Boot Starter](https://github.com/LogNet/grpc-spring-boot-starter)
as a gRPC implementation because it fits well with Spring Boot. I also used 
[Flyway DB](https://flywaydb.org/) migration library because it is simple, reliable 
and compatible with Spring Boot.

## Performance estimations
I didn't make performance estimations because of limited time.

## What could be done better
1. Error handling in the API could be improved.
2. [WalletController](src/main/java/com/sashashpota/wallet/controllers/WalletController.java)
could be covered with tests.
