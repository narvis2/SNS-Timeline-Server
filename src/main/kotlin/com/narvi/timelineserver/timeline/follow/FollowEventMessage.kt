package com.narvi.timelineserver.timeline.follow

data class FollowEventMessage(
    val userId: Long,
    val followerId: Long,
    val isFollow: Boolean,
)
