package com.takeaway.assignment.gameofthree.util.config;

import com.takeaway.assignment.gameofthree.util.properties.ApplicationConfiguration;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

  @Resource
  private ApplicationConfiguration applicationConfiguration;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {

    final var redis = applicationConfiguration.getRedis();
    var config = new RedisStandaloneConfiguration(redis.getHost(), redis.getPort());
    return new LettuceConnectionFactory(config);
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate() {
    RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory());
    return template;
  }
}