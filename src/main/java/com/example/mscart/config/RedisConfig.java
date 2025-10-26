package com.example.mscart.config;

import com.example.mscart.model.Cart;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

        // Универсальный RedisTemplate для всех объектов (включая Cart и Spring Session)
        @Bean
        public RedisTemplate<String, Cart> redisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, Cart> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);

            // Сериализация ключей → строки
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());

            // Сериализация значений → JSON
            GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(
                    new ObjectMapper().activateDefaultTyping(
                            LaissezFaireSubTypeValidator.instance,
                            ObjectMapper.DefaultTyping.NON_FINAL
                    )
            );
            template.setValueSerializer(serializer);
            template.setHashValueSerializer(serializer);

            template.afterPropertiesSet();
            return template;
        }

    @Bean
    public RedisTemplate<String, Object> redisTemplateObjects(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }


}
