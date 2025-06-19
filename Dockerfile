FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build.gradle.kts .
COPY gradle gradle
COPY gradlew .
COPY src ./src

RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

EXPOSE 8080

CMD ["java", "-jar", "build/libs/financial-control-1.0.0.jar"]
