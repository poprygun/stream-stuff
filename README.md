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

## Install Prometheus

```bash
brew install helm
helm repo add stable https://kubernetes-charts.storage.googleapis.com/
helm install my-prometheus stable/prometheus
export POD_NAME=$(kubectl get pods --namespace default -l "app=prometheus,component=server" -o jsonpath="{.items[0].metadata.name}")
kubectl --namespace default port-forward $POD_NAME 9090
```

## Install Grafana

```bash
helm install my-grafana stable/grafana
kubectl get secret --namespace default my-grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo

export POD_NAME=$(kubectl get pods --namespace default -l "app.kubernetes.io/name=grafana,app.kubernetes.io/instance=my-grafana" -o jsonpath="{.items[0].metadata.name}")
kubectl --namespace default port-forward $POD_NAME 3000
```

