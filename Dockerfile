FROM eclipse-temurin:17-jre
COPY target/my-spring-app-client-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]