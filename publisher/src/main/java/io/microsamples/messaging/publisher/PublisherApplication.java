package io.microsamples.messaging.publisher;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@SpringBootApplication
public class PublisherApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class, args);
    }

}

@RestController
@AllArgsConstructor
@Profile("pubsub")
class PubController {

    private final MessageChannel soundbitsChannel;

    @GetMapping("/sayit")
    public ResponseEntity sayIt() {
        soundbitsChannel.send(MessageBuilder.withPayload("Processing GET Request... " + Instant.now()).build());
        return ResponseEntity.ok("Request processed.");
    }
}

@RestController
@Profile("sync")
class SyncController {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.cloud.stream.bindings.soundbitsChannel.destination}")
    private String syncExchange;

    public SyncController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sayit")
    public ResponseEntity sayIt() {

        final Object response = rabbitTemplate
                .convertSendAndReceive(
                        syncExchange
                        , "soundbits.key"
                        , "Processing GET Request... " + Instant.now()
                );

        return ResponseEntity.ok("Publisher: Response received. " + response.toString());
    }
}


interface Channels {
    @Output("soundbitsChannel")
    MessageChannel outgoing();
}
