# Spring Cloud Stream with Zipkin monitoring

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
docker run -d --name some-rabbit -p 5672:5672 -p 5673:5673 -p 15672:15672 rabbitmq:3-management
```

## Zipkin server

Span data from publisher and subscriber services should appear in [zipkin console](http://localhost:9411/zipkin)

## Gatling Stress Test

### Prerequisites

- Install sbt

```bash
brew isntall sbt
```

- Use java 12

```bash
jenv local 12
```

### Install Gatling AMQP Plugin to local Maven repository

```bash
cd gatling-amqp-plugin/
sbt publishM2
```

### Loader configuration

see gatling-runner/README.md

Monitor RabbitMQ in [Console](http://localhost:15672/#/)

Simulate load 
```bash
curl -s "localhost:8080/sayit?[1-10]"
```

## To start minikube

```bash
minikube start --memory=8192 --cpus=2 --kubernetes-version=v1.18.0 --vm-driver=hyperkit --bootstrapper=kubeadm --extra-config=apiserver.enable-admission-plugins="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook"
```