FROM maven:3.6.3-openjdk-14-slim

COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn clean package

FROM tomcat:9.0.35
COPY --from=builder /target/composite-category-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
COPY ./conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml

EXPOSE 8081
