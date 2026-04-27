FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven
RUN mvn clean install -DskipTests

CMD ["java", "-jar", "target/FSDproject-0.0.1-SNAPSHOT.jar"]