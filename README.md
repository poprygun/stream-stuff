# POC for evaluating communication options between services

## Prerequisites

1. Install Docker
2. Install Kubernetes
3. Use notes for installing Prometheus and Grafana below

### To start minikube

```bash
minikube start --memory=8192 --cpus=2 --kubernetes-version=v1.18.0 --vm-driver=hyperkit --bootstrapper=kubeadm --extra-config=apiserver.enable-admission-plugins="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook"
```

### Install Prometheus

```bash
brew install helm
helm repo add stable https://kubernetes-charts.storage.googleapis.com/
helm install my-prometheus stable/prometheus
export POD_NAME=$(kubectl get pods --namespace default -l "app=prometheus,component=server" -o jsonpath="{.items[0].metadata.name}")
kubectl --namespace default port-forward $POD_NAME 9090
```

### Install Grafana

```bash
helm install my-grafana stable/grafana
kubectl get secret --namespace default my-grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo

export POD_NAME=$(kubectl get pods --namespace default -l "app.kubernetes.io/name=grafana,app.kubernetes.io/instance=my-grafana" -o jsonpath="{.items[0].metadata.name}")
kubectl --namespace default port-forward $POD_NAME 3000
```

## Services Setup

`pom.xml` files for each project uses jib plugin to build an image and deploy to dockerhub.  Please adjust to use your private docker repository before building all projects.
`pulisher`, `subscriber-publisher` and `subscriber` projects can be ran in either `sync` or `pubsub` profiles for RPC or Async RabbitMQ modes of communication.

## Start all services

From project directory, run:

```bash
kubectl apply -f deploy
```

Monitor logs of all services to make sure they started successfully.

```bash
kubectl log -f -l run=traffic-cop
kubectl log -f -l run=publisher
kubectl log -f -l run=subscriber
kubectl log -f -l run=http-listener
```

## Gatling Stress Test - [Traffic Cop](https://github.com/walterscarborough/traffic-cop)

Follow Traffic Cop README for setup details.  It should work out of the box if started without errors.

## Simulate load

```bash
curl --location --request POST 'http://192.168.64.4:31889/run-load-test' \
--header 'Content-Type: application/json' \
--data-raw '{
    "baseUrl": "http://192.168.64.4:31999",
    "endpoint": "/sayit",
    "httpMethod": "GET",
    "payload": "{}",
    "constantUsersPerSecond": 20,
    "constantUsersPerSecondDuration": 10,
    "rampUsersPerSecondMinimum": 20,
    "rampUsersPerSecondMaximum": 20,
    "rampUsersPerSecondDuration": 20
}'
```

## Use following links to access tools running on Minikube

### Determine minikube IP

```bash
export MINIKUBE_IP=$(minikube ip)
```

[zipkin](http://MINIKUBE_IP:31411/)
[rabbit mq console](http://MINIKUBE_IP:31672)
[traffic cop reports](http://MINIKUBE_IP:31889/reports)
[grafana](http://localhost:3000)
[prometheus](http://localhost:9090)