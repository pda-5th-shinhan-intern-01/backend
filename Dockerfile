FROM openjdk:17
WORKDIR /app
COPY ./build/libs/hotsignal-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]