package io.microsamples.messaging.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@SpringBootApplication
public class PublisherApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class, args);
    }

}

@Component
@Slf4j
class ChatterBox {

	private final MessageChannel outgoing;

	public ChatterBox(Channels channels) {
		this.outgoing = channels.outgoing();
	}

	@Scheduled(initialDelay = 1000, fixedRate = 10000)
	public void saySomething(){
		outgoing.send(MessageBuilder.withPayload("I am talking to ya... " + Instant.now()).build());
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
