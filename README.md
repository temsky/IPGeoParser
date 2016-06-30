IP Geo Parser
=====

This is simple tool what help you to locate country, city, isp by ip address.
Built with Java 8, STOMP over websockets, Spring Boot, Spring Data JPA.

Advantages:
* bulk data analysis
* accurate results
* saving data to PostgreSQL
 
Disadvantages:
* a little bit slow

It used free api geo services.

## Postgres Instance Configuration
Create file 'application.properties'  and put it to: ```src/main/resources/```
```
#
# [ Database Configuration Section ]
#
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/dbip
spring.datasource.username=postgres
spring.datasource.password=password
#
# [ Other Configuration Attributes ]
#
```

## Demo/Examples
The live version of this solution can be found at: [ip-geo.ru](http://ip-geo.ru)

 
