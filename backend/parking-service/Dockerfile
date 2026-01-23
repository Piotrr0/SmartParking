FROM eclipse-temurin:17-jdk-focal
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests -pl parking-service -am
ENTRYPOINT ["java","-jar","parking-service/target/parking-service-0.0.1-SNAPSHOT.jar"]
