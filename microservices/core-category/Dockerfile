#FROM maven:3.6.3-openjdk-14-slim
#VOLUME /root/.m2
#
#COPY ./pom.xml /target/pom.xml
#COPY ./src /target/src
#RUN mvn clean package -f /target/pom.xml
#CMD ["java", "-jar", "/target/core-category-0.0.1-SNAPSHOT.jar"]

FROM openjdk:14.0.1-slim
#VOLUME ["/build"]
#WORKDIR /build
COPY target/buildfile.jar /app/buildfile.jar
WORKDIR /app

# EXPOSE 8080
#RUN PWD
# RUN ls -la
#RUN java -jar buildfile.jar
CMD ["java", "-jar", "buildfile.jar"]
