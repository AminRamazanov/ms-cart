package com.example.mscart.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String CART_EXC = "CART_EXC";
    public static final String CART_DLX = "CART_DLX";

    public static final String CART_ADD_QUEUE = "cart.add.queue";
    public static final String CART_ADD_DLQ = "cart.add.dlq";


    public static final String CART_ADD_ROUTING_KEY = "cart.add";
    public static final String CART_ADD_DLQ_KEY = "cart.add.dlq.key";


    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(CART_EXC);
    }

    @Bean
    public TopicExchange dlqExchange(){
        return new TopicExchange(CART_DLX);
    }

    @Bean
    public Queue fetchCustomBouquetQueue(){
        return QueueBuilder.durable(CART_ADD_QUEUE)
                .withArgument("x-dead-letter-exchange", CART_DLX)
                .withArgument("x-dead-letter-routing-key", CART_ADD_DLQ_KEY)
                .build();
    }

    @Bean
    public Queue dlqcustomBouquetQueue(){
        return QueueBuilder.durable(CART_ADD_DLQ).build();
    }

    @Bean
    public Binding bindingCustomBouquetQueue(){
        return BindingBuilder.bind(fetchCustomBouquetQueue()).to(exchange()).with(CART_ADD_ROUTING_KEY);
    }

    @Bean
    public Binding bindingDlqCustomBouquetQueue(){
        return BindingBuilder.bind(dlqcustomBouquetQueue()).to(dlqExchange()).with(CART_ADD_DLQ_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter());
        return factory;
    }
}
