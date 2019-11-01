# Kafka Load Emulator

## Configuration

You can add as many load simulations as you would like.
See `gatling-kafka/src/test/scala/com/github/mnogu/gatling/kafka/test/ChachkiesPublishSimulation.scala` for an example.

Messages are posted to kafka topic called `soundbits`

[Sample consumer](https://github.com/poprygun/stream-stuff/tree/kafka-integration/subscriber)

## Setup

Run

```bash
./scripts/setup-gatling-kafka.sh
```

## Run

Run

```bash
./scripts/gatling.sh
```