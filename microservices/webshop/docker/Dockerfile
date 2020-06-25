FROM maven:3.6-jdk-11 as builder

COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn clean package

FROM tomcat:9.0
COPY --from=builder /target/*.war /usr/local/tomcat/webapps/webshop.war
COPY ./conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
# COPY ./conf/context.xml /opt/bitnami/tomcat/conf/Catalina/localhost/context.xml
# COPY ./conf/context.xml /bitnami/tomcat/data/manager/META-INF/context.xml
# COPY ./conf/context.xml /opt/bitnami/tomcat/webapps/manager/META-INF/context.xml


EXPOSE 8080

# CMD ["catalina.sh", "run"]
