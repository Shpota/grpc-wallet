# Wallet gRPC API implementation
A simple balance gRPC API implementation with Spring Boot and MySQL

Start MySQL:
```
docker run --name wallet-db \
  -e MYSQL_DATABASE=wallet \
  -e MYSQL_USER=wallet \
  -e MYSQL_PASSWORD=wallet \
  -e MYSQL_ROOT_PASSWORD=wallet \
  -p 3306:3306 \
  mysql:5.7.26 
```
