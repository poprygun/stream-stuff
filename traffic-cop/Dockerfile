FROM openjdk:8-jdk-alpine

EXPOSE 8080

RUN mkdir /app

COPY . /app

WORKDIR /app

RUN ./gradlew tasks

ENTRYPOINT ["./gradlew", "bootRun"]