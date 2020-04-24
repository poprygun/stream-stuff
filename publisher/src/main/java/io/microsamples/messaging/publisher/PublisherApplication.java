package io.microsamples.messaging.publisher;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
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
@Slf4j
class PubController {

    private final MessageChannel soundbitsChannel;

    @GetMapping("/sayit")
    public ResponseEntity sayIt() {
        soundbitsChannel.send(MessageBuilder.withPayload("Processing GET Request... " + Instant.now()).build());
        return ResponseEntity.ok("Request processed.");
    }


    @StreamListener(Sink.INPUT)
    public void handleMessage(Message<String> message) {
        log.info("Received response from http-listener {}", message.getPayload());
    }
}

@RestController
@Profile("sync")
@Slf4j
class SyncController {


    private final SyncSender syncSender;

    public SyncController(SyncSender syncSender) {
        this.syncSender = syncSender;
    }

    @GetMapping("/sayit")
    public ResponseEntity sayIt() {

        final Object response = syncSender.sendIt();

        final String rtn = "Publisher: Response received. " + response.toString();
        log.info(rtn);
        return ResponseEntity.ok(rtn);
    }
}


interface Channels {
    @Output("soundbitsChannel")
    MessageChannel outgoing();
}
