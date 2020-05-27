#eShop API
Authoren: Johanna Bräuning, Joan-Angelo Douvere
## TODO
### Prio
- 1 Docker-Compose
  - auf HOST Ebene builden
  - jars in Container kopieren
  - depends on einfügen für Abhängigkeiten -> Eureka zuerst
- 2 Zuul
  - Nur eShop API sollte exposed sein, 
  - andere nicht als Zuul-Clients
- 3 Integrität absichern
  - Wenn man Kategorien löscht, sollten Produkte auch geändert/gelöscht werden
  - Produkte, User, Roles genauso
- 4 Hystrix Fallbacks für Get-Methoden / Cache

### Nice to have
- Geteilte Komponenten in einem eigenständigen Projekt auslagern und als Abhängigkeit in die Microservices einbinden
- Update-Methoden (PUT)
- bugs fixen..


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
