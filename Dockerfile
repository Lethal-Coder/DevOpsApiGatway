FROM eclipse-temurin:17-jre
COPY target/my-spring-app-client-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/app.jar"]