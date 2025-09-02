package com.narvi.timelineserver.kafka

import com.narvi.timelineserver.common.logging.Logging
import com.narvi.timelineserver.common.utils.JsonUtil
import com.narvi.timelineserver.timeline.feed.FeedInfo
import com.narvi.timelineserver.timeline.follow.FollowEventMessage
import com.narvi.timelineserver.timeline.follow.FollowerStore
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class FollowerConsumer(
    private val jsonUtil: JsonUtil,
    private val followerStore: FollowerStore,
) {
    private val logger = Logging.getLogger(FollowerConsumer::class.java)

    @KafkaListener(
        topics = ["user.follower"],
        groupId = "timeline-server",
        concurrency = "3"
    )
    fun listen(
        consumerRecord: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment,
    ) {
        Logging.logFor(logger) { log ->
            try {
                log["record"] = consumerRecord.value()
                log["topic"] = consumerRecord.topic()
                log["key"] = consumerRecord.key()
                log["partition"] = consumerRecord.partition()
                log["offset"] = consumerRecord.offset()

                jsonUtil.fromJson(consumerRecord.value(), FollowEventMessage::class.java)?.let(followerStore::followUser)
            } catch (ex: Exception) {
                logger.error("Record handling failed. cause: {}", ex.message)
            } finally {
                log["offset committed"] = "Offset Commit"
                acknowledgment.acknowledge() // Offset Commit
            }
        }
    }
}