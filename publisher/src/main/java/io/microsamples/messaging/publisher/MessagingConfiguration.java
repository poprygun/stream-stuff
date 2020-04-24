package io.microsamples.messaging.publisher;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("sync")
public class MessagingConfiguration {

    @Value("${amqp.sync.reply.timeout:20000}")
    private long replyTimeout;

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new SimpleMessageConverter());
        rabbitTemplate.setReplyTimeout(replyTimeout);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter(){
        return new SimpleMessageConverter();
    }
}
@Configuration
@EnableBinding({Sink.class, Channels.class})
@Profile("pubsub")
class PubSubConfiguration{}