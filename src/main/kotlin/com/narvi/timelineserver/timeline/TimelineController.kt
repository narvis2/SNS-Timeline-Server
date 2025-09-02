package com.narvi.timelineserver.timeline

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/timeline")
class TimelineController(
    private val timelineService: TimelineService,
) {

    // 전체 피드 조회
    @GetMapping
    fun listAllFeed(): List<SocialPost> = timelineService.listAllFeed()

    // 사용자가 올린 피드들을 조회하는 API
    @GetMapping("/{userId}")
    fun listFeed(
        @PathVariable(required = true)
        userId: String,
        @RequestParam(value = "followingFeed", required = false)
        includeFollowingFeed: Boolean?, // 내가 팔로우하는 사람들의 피드도 같이 섞어서 보여줄지
        @RequestParam(value = "randomPost", required = false, defaultValue = "0")
        randomPost: Double // 내가 팔로우하지 않은 사람들의 필드도 같이 보여주기
    ): List<SocialPost> {
        if (randomPost > 0) {
            return timelineService.getRandomPost(userId, randomPost)
        }

        return if (includeFollowingFeed == true) {
            timelineService.listMyFeed(userId)
        } else {
            timelineService.listUserFeed(userId)
        }
    }

    // 좋아요 누르기
    @GetMapping("/like/{postId}/{userId}")
    fun likePost(
        @PathVariable("postId", required = true)
        postId: Long,
        @PathVariable("userId", required = true)
        userId: Long
    ): LikeResponse {
        val isLike = timelineService.likePost(userId = userId, postId = postId)
        val count = timelineService.countLike(postId)
        return LikeResponse(likeCount = count, like = isLike)
    }
}