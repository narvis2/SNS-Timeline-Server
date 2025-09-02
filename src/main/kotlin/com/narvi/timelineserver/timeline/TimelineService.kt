package com.narvi.timelineserver.timeline

import com.narvi.timelineserver.timeline.feed.FeedStore
import com.narvi.timelineserver.timeline.follow.FollowerStore
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class TimelineService(
    private val feedStore: FeedStore,
    private val followerStore: FollowerStore,
    private val socialPostMapper: SocialPostMapper
) {

    fun listUserFeed(userId: String): List<SocialPost> {
        val feedList = feedStore.listFeed(userId)
        val likes = feedStore.countLikes(feedList.map { it.feedId }.toList())

        return feedList.map { post ->
            socialPostMapper.feedInfoToSocialPostWithLike(post, likes)
        }
    }

    fun getRandomPost(userId: String, randomPost: Double): List<SocialPost> {
        val myPost = if (userId == "none") emptyList() else listMyFeed(userId)
        val randomPostSize = 10.coerceAtLeast(ceil(myPost.size * randomPost).toInt())

        val allPost = ArrayList<SocialPost>(listAllFeed())

        val myPostIds = myPost.map { it.feedId }.toSet()

        allPost.removeIf { it.feedId in myPostIds }

        val picked = if (randomPostSize >= allPost.size) {
            allPost
        } else {
            allPost.shuffle()
            allPost.subList(0, randomPostSize)
        }

        allPost.removeIf { it.feedId in myPostIds }

        return (myPost + picked).sortedByDescending {
            it.uploadDateTime
        }
    }

    fun listMyFeed(userId: String): List<SocialPost> {
        val followers = followerStore.listFollower(userId)
        val myPosts = listUserFeed(userId)
        val followerPosts = listFollowerFeed(followers)

        return (myPosts + followerPosts).sortedByDescending {
            it.uploadDateTime
        }
    }

    fun listFollowerFeed(followers: Set<String>): List<SocialPost> {
        return followers.flatMap(::listUserFeed)
    }

    fun listAllFeed(): List<SocialPost> {
        val feedList = feedStore.allFeed()
        val likes = feedStore.countLikes(feedList.map { it.feedId }.toList())
        return feedList.map { post ->
            socialPostMapper.feedInfoToSocialPostWithLike(post, likes)
        }
    }

    fun likePost(userId: Long, postId: Long): Boolean = if (feedStore.isLikePost(userId, postId)) {
        feedStore.unlikePost(userId, postId)
        false
    } else {
        feedStore.likePost(userId, postId)
        true
    }

    fun countLike(postId: Long): Long = feedStore.countLikes(postId) ?: 0L
}