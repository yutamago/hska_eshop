#eShop API
Authoren: Johanna Bräuning, Joan-Angelo Douvere
## TODO
### Prio
- 3.2 b) Ribbon Clients / Client-side Load-Balancing testen
- 3.2 c) Edge Server (Zuul) auslagern in echten Service
- 3.3 Docker-Compose
- 3 Skizze der implementierten Systemarchitektur
- 3 Präsentation vorbereiten (Routing, Monitoring, Load-Balancing, Circuit Breaking)

### Nice to have
- 3.1 c) Geteilte Komponenten können in einem eigenständigen Projekt realisiert werden, dass dann als Abhängigkeit in die Microservices eingebunden wird.

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
