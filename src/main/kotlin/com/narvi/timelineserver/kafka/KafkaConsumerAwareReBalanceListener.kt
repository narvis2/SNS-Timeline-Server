package com.narvi.timelineserver.kafka

import com.narvi.timelineserver.common.logging.Logging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.TopicPartition
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumerAwareReBalanceListener : ConsumerAwareRebalanceListener {

    private val logger = Logging.getLogger(KafkaConsumerAwareReBalanceListener::class.java)


    // Consumer 가 할당될떄 호출됨
    override fun onPartitionsAssigned(consumer: Consumer<*, *>, partitions: MutableCollection<TopicPartition>) {
        Logging.logFor(logger) { log ->
            log["consumer"] = consumer.toString()
            log["assigned"] = partitions.toString()
        }
    }

    // Consumer 가 회수될때 호출됨
    override fun onPartitionsRevoked(partitions: MutableCollection<TopicPartition>) {
        Logging.logFor(logger) { log ->
            log["Kafka consumer revoked"] = partitions.toString()
        }
    }
}