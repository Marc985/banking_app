FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY .. .
RUN mvn clean package -Dskiptests

FROM openjdk:21-jdk
COPY --from=build /target/wallet-app-0.0.1-SNAPSHOT.jar wallet-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","wallet-app.jar"]
