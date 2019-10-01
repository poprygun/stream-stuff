# Spring Cloud Stream with Zipkin monitoring

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
docker run -d --name some-rabbit -p 5672:5672 -p 5673:5673 -p 15672:15672 rabbitmq:3-management
```

## Zipkin server

Span data from publisher and subscriber services should appear in [zipkin console](http://localhost:9411/zipkin)