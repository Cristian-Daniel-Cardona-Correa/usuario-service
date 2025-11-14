FROM eclipse-temurin:17-jdk-jammy
VOLUME /tmp
EXPOSE 8082
ARG JAR_FILE=target/usuario-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]