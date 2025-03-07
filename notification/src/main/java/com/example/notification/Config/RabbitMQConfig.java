package com.example.notification.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    String exchangeName = "default";  // Match this with StudentService

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    Queue firstStepQueue() {
        return new Queue("firstStepQueue", true);  // Ensure this matches the queue in StudentService
    }

    @Bean
    Binding firstStepBinding(Queue firstStepQueue, DirectExchange exchange) {
        return BindingBuilder.bind(firstStepQueue).to(exchange).with("firstStepRoute");  // Same routing key
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
