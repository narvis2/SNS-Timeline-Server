package com.narvi.timelineserver.config.kafka

import com.narvi.timelineserver.common.logging.Logging
import com.narvi.timelineserver.kafka.KafkaConsumerAwareReBalanceListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.listener.ContainerProperties

@Configuration
class KafkaConfig {

    private val logger = Logging.getLogger(KafkaConfig::class.java)

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
        awareReBalanceListener: KafkaConsumerAwareReBalanceListener,
    ): ConcurrentKafkaListenerContainerFactory<String, String> {
        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()
        containerFactory.consumerFactory = consumerFactory
        // Consumer 가 메시지를 읽은 후 오프셋을 언제 커밋할지를 결정하는 설정
        containerFactory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
        containerFactory.containerProperties.setConsumerRebalanceListener(awareReBalanceListener)

        Logging.logFor(logger) { log ->
            log["set AckMode"] = containerFactory.containerProperties.ackMode
        }
        return containerFactory
    }
}