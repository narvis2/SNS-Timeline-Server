package com.narvi.timelineserver.timeline

import java.time.ZonedDateTime

data class SocialPost(
    val feedId: Long,
    val imageId: String,
    val uploaderName: String,
    val uploaderId: Long,
    val uploadDateTime: ZonedDateTime? = null,
    val contents: String? = null,
    val likes: Long,
)
