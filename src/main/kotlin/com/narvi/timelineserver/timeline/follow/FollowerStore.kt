package com.narvi.timelineserver.timeline.follow

import com.narvi.timelineserver.common.utils.JsonUtil
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class FollowerStore(
    private val redisTemplate: RedisTemplate<String, String>,
    private val jsonUtil: JsonUtil,
) {

    fun followUser(followEventMessage: FollowEventMessage) {

    }

    fun listFollower(userId: String): Set<String> {
        return setOf()
    }
}