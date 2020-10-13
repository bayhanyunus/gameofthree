package com.takeaway.assignment.gameofthree.util.config;

import com.takeaway.assignment.gameofthree.util.properties.ApplicationConfiguration;
import com.takeaway.assignment.gameofthree.util.properties.RabbitMQConfiguration;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {

  private final RabbitMQConfiguration rabbitMQ;

  public RabbitMQConfig(ApplicationConfiguration applicationConfiguration) {
    this.rabbitMQ = applicationConfiguration.getRabbitMq();
  }

  @Bean("cachingConnectionFactory")
  public CachingConnectionFactory getCachingConnectionFactory() {

    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQ.getHost(), rabbitMQ.getPort());
    connectionFactory.setUsername(rabbitMQ.getUser());
    connectionFactory.setPassword(rabbitMQ.getPassword());
    return connectionFactory;
  }

  @Bean("gameRabbitTemplate")
  public RabbitTemplate gameRabbitTemplate(@Qualifier("cachingConnectionFactory") CachingConnectionFactory connectionFactory) {

    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setExchange(rabbitMQ.getExchange());
    rabbitTemplate.setRoutingKey(rabbitMQ.getRoutingKey());
    return rabbitTemplate;
  }

  @Bean
  public DirectExchange gameExchange() {
    return new DirectExchange(rabbitMQ.getExchange(), true, false);
  }

  @Bean
  public Queue gameQueue() {
    return new Queue(rabbitMQ.getQueue(), true);
  }

  @Bean
  public Binding gameExchangeBinding(DirectExchange exchange, Queue queue) {
    return BindingBuilder.bind(queue).to(exchange).with(rabbitMQ.getRoutingKey());
  }

  @Bean(name = "gameAmqpAdmin")
  public AmqpAdmin gameAmqpAdmin(@Qualifier("cachingConnectionFactory") ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

}