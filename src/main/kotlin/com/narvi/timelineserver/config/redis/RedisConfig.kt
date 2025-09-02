package com.narvi.timelineserver.config.redis

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig {

    @Bean
    fun redisConnectionFactory(
        props: RedisProperties
    ): LettuceConnectionFactory {
        val clusterNodes = props.cluster?.nodes ?: emptyList()
        val clusterConfig = RedisClusterConfiguration(clusterNodes)
        val clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofMillis(10000))
            .build()

        return LettuceConnectionFactory(clusterConfig, clientConfig)
    }

    @Bean
    @Primary
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, String> =
        RedisTemplate<String, String>().apply {
            setConnectionFactory(connectionFactory)
            keySerializer = StringRedisSerializer()
            valueSerializer = Jackson2JsonRedisSerializer(String::class.java)
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = Jackson2JsonRedisSerializer(String::class.java)
            afterPropertiesSet()
        }

}