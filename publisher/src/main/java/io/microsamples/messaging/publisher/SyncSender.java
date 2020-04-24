package io.microsamples.messaging.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SyncSender {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.cloud.stream.bindings.soundbitsChannel.destination}")
    private String syncExchange;

    public SyncSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendIt(){
        rabbitTemplate
                .convertAndSend(
                        syncExchange
                        , "soundbits.key"
                        , "Processing GET Request... "
                );
    }
}
