package io.microsamples.messaging.publisher;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
class PubController {

    private final MessageChannel soundbitsChannel;

    @GetMapping("/sayit")
    public ResponseEntity sayIt() {
        soundbitsChannel.send(MessageBuilder.withPayload("Processing GET Request... " + Instant.now()).build());
        return ResponseEntity.ok("Request processed.");
    }
}

@Component
@AllArgsConstructor
@Slf4j
class ChatterBox {

    private final MessageChannel soundbitsChannel;

    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void saySomething() {
        final Instant now = Instant.now();
        log.info("Publishing message at {}", now);
        soundbitsChannel.send(MessageBuilder.withPayload("I am talking to ya... " + now).build());
    }
}

@Configuration
@EnableBinding(Channels.class)
@EnableScheduling
class MessagingConfiguration {

}

interface Channels {
    @Output("soundbitsChannel")
    MessageChannel outgoing();
}
