#eShop API
Authoren: Johanna Bräuning, Joan-Angelo Douvere

## Build & Run
1. `buildall.cmd`
2. `docker-compose up --build`

## TODO
### Prio

### Nice to have
- Geteilte Komponenten in einem eigenständigen Projekt auslagern und als Abhängigkeit in die Microservices einbinden
- Update-Methoden (PUT)
- bugs fixen..
- Cache aktualisieren nach Löschen/Ändern


## Links

### Dienst-Routen
- http://localhost:8080/swagger-ui.html
- http://localhost:8080/composite-category/swagger-ui.html 
- http://localhost:8080/composite-product/swagger-ui.html
- http://localhost:8080/core-category/swagger-ui.html
- http://localhost:8080/core-product/swagger-ui.html
- http://localhost:8080/core-user/swagger-ui.html

### Dienste
- **eShop API:**            http://localhost:8080/swagger-ui.html
- **Composite Category:**   http://localhost:8081/swagger-ui.html
- **Composite Product:**    http://localhost:8082/swagger-ui.html
- **Core Category:**        http://localhost:8084/swagger-ui.html
- **Core Product:**         http://localhost:8085/swagger-ui.html
- **Core User:**            http://localhost:8086/swagger-ui.html

### Hystrix Dashboard
- http://localhost:8080/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8080%2Factuator%2Fhystrix.stream&title=api
