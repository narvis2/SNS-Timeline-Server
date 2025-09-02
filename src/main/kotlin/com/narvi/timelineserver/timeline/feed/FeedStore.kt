package com.narvi.timelineserver.timeline.feed

import com.narvi.timelineserver.common.utils.JsonUtil
import org.springframework.data.redis.connection.StringRedisConnection
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class FeedStore(
    private val redisTemplate: RedisTemplate<String, String>,
    private val jsonUtil: JsonUtil,
) {

    fun savePost(post: FeedInfo) {
        jsonUtil.toJson(post)?.let { json ->
            // 정렬가능한 Set
            redisTemplate.opsForZSet()
                .add("feed:${post.uploaderId}", json, post.uploadDateTime.toEpochSecond().toDouble())

            redisTemplate.opsForZSet()
                .add("feed:all", json, post.uploadDateTime.toEpochSecond().toDouble())
        }
    }

    fun allFeed(): List<FeedInfo> {
        val savedFeeds = redisTemplate.opsForZSet().reverseRange("feed:all", 0 , -1) ?: emptySet()
        return savedFeeds.mapNotNull { feed ->
            jsonUtil.fromJson(feed, FeedInfo::class.java)
        }
    }

    fun listFeed(userId: String): List<FeedInfo> {
        val savedFeeds = redisTemplate.opsForZSet().reverseRange("feed:$userId", 0 , -1) ?: emptySet()
        return savedFeeds.mapNotNull { feed ->
            jsonUtil.fromJson(feed, FeedInfo::class.java)
        }
    }

    fun likePost(userId: Long, postId: Long): Long? = redisTemplate.opsForSet().add("likes:$postId", userId.toString())

    fun unlikePost(userId: Long, postId: Long): Long? =
        redisTemplate.opsForSet().remove("likes:$postId", userId.toString())

    fun isLikePost(userId: Long, postId: Long): Boolean =
        redisTemplate.opsForSet().isMember("likes:$postId", userId.toString()) ?: false

    fun countLikes(postId: Long) = redisTemplate.opsForSet().size("likes:$postId")

    fun countLikes(postIds: List<Long>): Map<Long, Long> {
        val results = redisTemplate.executePipelined(RedisCallback<Any> { connection ->
            val conn = connection as StringRedisConnection
            postIds.forEach { postId -> conn.sCard("likes:$postId") }
            null
        })

        return postIds.zip(results).associate { (postId, res) ->
            postId to (res as Number).toLong()
        }
    }
}