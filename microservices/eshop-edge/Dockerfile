FROM maven:3.6.3-openjdk-14-slim
VOLUME /root/.m2

COPY ./pom.xml /target/pom.xml
COPY ./src /target/src
RUN mvn clean package -f /target/pom.xml
CMD ["java", "-jar", "/target/eshop-edge-0.0.1-SNAPSHOT.jar"]

EXPOSE 9001:9001
