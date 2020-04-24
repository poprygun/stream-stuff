package io.microsamples.messaging.subscriber;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

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
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Configuration
@EnableBinding({Sink.class, Channels.class})
@Profile("pubsub")
class PubSubConfiguration{
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
