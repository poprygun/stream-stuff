package io.microsamples.messaging.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SubscriberApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriberApplication.class, args);
	}

}
@Configuration
@EnableBinding(Sink.class)
class MessagingConfiguration {

}

@Component
@Slf4j
class Poliglot{
	@StreamListener(Sink.INPUT)
	public void handleMessage(Message<String> message) {
		log.info("Received: {}.", message.getPayload());
	}
}