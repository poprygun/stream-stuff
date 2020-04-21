package io.microsamples.messaging.subscriber;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new SimpleMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue queue(@Value("${spring.cloud.stream.bindings.input.destination}") String queue) {
        return new Queue(queue);
    }

    @Bean
    public TopicExchange directExchange(@Value("${spring.cloud.stream.bindings.input.destination}") String exchange) {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding binding(TopicExchange exchange
         , Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("soundbits.key");
    }
}

@Configuration
@EnableBinding({Sink.class, Channels.class})
@Profile("pubsub")
class PubSubConfiguration{}
