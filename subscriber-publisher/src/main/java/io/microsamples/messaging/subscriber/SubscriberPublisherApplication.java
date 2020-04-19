package io.microsamples.messaging.subscriber;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;
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
@Configuration
@EnableBinding({Sink.class, Channels.class})
class MessagingConfiguration {

}

@Component
@AllArgsConstructor
@Slf4j
class Poliglot{
	private final MessageChannel soundbitsChannel;
	@StreamListener(Sink.INPUT)
	public void handleMessage(Message<String> message) {
		log.info("Received and Resent: {} {}", message.getHeaders(), message.getPayload());
		soundbitsChannel.send(message);
	}
}

interface Channels {
	@Output("soundbitsChannel")
	MessageChannel outgoing();
}