package io.microsamples.messaging.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@SpringBootApplication
public class SubscriberApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriberApplication.class, args);
    }

}

@Component
@Slf4j
@Profile("pubsub")
class Poliglot {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MessageChannel responseChannel;

    @Value("${http-listener.url}")
    private String url;

    @Value("${http-listener.delay:0}")
    private int delay;

    @StreamListener(Sink.INPUT)
    public void handleMessage(Message<String> message) {
        final String url = this.url.concat("/echo");
        log.info("Received from publisher {}. Posting to {}", message.getPayload(), url);
        final ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class, delay);

        final String payload = "Response from http-listener: " + forEntity.getBody();
        responseChannel.send(MessageBuilder
                .withPayload(payload).build());
        log.info("Published response {}", payload);
    }
}

interface Channels {
    @Output("responseChannel")
    MessageChannel outgoing();
}

@Component
@Slf4j
@Profile("sync")
class SyncPoliglot{

    @Autowired
    private RestTemplate restTemplate;

    @Value("${http-listener.url}")
    private String url;

    @Value("${http-listener.delay:0}")
    private int delay;

    @RabbitListener(queues = "${spring.cloud.stream.bindings.input.destination}")
    public String handleMessage(Message<String> message) {
        log.info("Received: {}", message.getPayload());
        final String url = this.url.concat("/echo");
        log.info("Posting to {}", url);
        return "Subscriber: " + restTemplate.getForEntity(url, String.class, delay).getBody();
    }
}