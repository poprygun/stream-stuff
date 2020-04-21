package io.microsamples.messaging.subscriber;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@SpringBootApplication
public class SubscriberPublisherApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriberPublisherApplication.class, args);
	}

}

@Component
@AllArgsConstructor
@Slf4j
@Profile("pubsub")
class Poliglot{
	private final MessageChannel soundbitsChannel;
	@StreamListener(Sink.INPUT)
	public void handleMessage(Message<String> message) {
		log.info("Received and Resent: {} {}", message.getHeaders(), message.getPayload());
		soundbitsChannel.send(message);
	}
}

@Component
@Slf4j
@Profile("sync")
class SyncPoliglot{

	private final RabbitTemplate rabbitTemplate;

	@Value("${spring.cloud.stream.bindings.soundbitsChannel.destination}")
	private String syncExchange;

	public SyncPoliglot(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@RabbitListener(queues = "${spring.cloud.stream.bindings.input.destination}")
	public String handleMessage(String message){
		log.info("receved {}", message);
		final String response = (String)rabbitTemplate
				.convertSendAndReceive(
						syncExchange
						, "soundbits.key"
						, "Processing GET Request... " + Instant.now()
				);
		return "SubscriberPublisher: " + response;
	}
}


interface Channels {
	@Output("soundbitsChannel")
	MessageChannel outgoing();
}