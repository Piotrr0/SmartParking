FROM eclipse-temurin:17-jdk-focal
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests -pl user-service -am
ENTRYPOINT ["java","-jar","user-service/target/user-service-0.0.1-SNAPSHOT.jar"]
