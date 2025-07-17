FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8443




