# FROM openjdk:8-jdk-alpine
# COPY target/user-notification-service-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:17-alpine as base
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
COPY src ./src
# CMD ["./mvnw","clean", "install"]

FROM base as development
CMD ["./mvnw", "spring-boot:run","-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]