# Spring Cloud Stream with Zipkin monitoring

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
docker run -d --name some-rabbit -p 5672:5672 -p 5673:5673 -p 15672:15672 rabbitmq:3-management
```

## Zipkin server

Span data from publisher and subscriber services should appear in [zipkin console](http://localhost:9411/zipkin)

## Gatling Stress Test - [Traffic Cop](https://github.com/walterscarborough/traffic-cop)

Follow Traffic Cop README for setup details.

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

## To start minikube

```bash
minikube start --memory=8192 --cpus=2 --kubernetes-version=v1.18.0 --vm-driver=hyperkit --bootstrapper=kubeadm --extra-config=apiserver.enable-admission-plugins="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook"
```

_Follow instructions to install Prometheus and Grafana using Helm_

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

### Grafana console can now be accessed via http://localhost:3000


## Use following links to access tools running on Minikube

### Determine minikube IP

```bash
export MINIKUBE_IP=$(minikube ip)
```

[zipkin](http://MINIKUBE_IP:31411/)
[rabbit mq console](http://MINIKUBE_IP:31672)
[traffic cop reports](http://MINIKUBE_IP:31889/reports)