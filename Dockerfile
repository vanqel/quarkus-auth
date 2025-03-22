FROM eclipse-temurin:21-jre as jre

COPY /build/quarkus-app /app

ENTRYPOINT ["sh", "-c", "java -jar /app/quarkus-run.jar"]
