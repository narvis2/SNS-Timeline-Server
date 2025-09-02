package com.narvi.timelineserver.timeline.feed

import java.time.ZonedDateTime

data class FeedInfo(
    val feedId: Long,
    val imageId: String,
    val uploaderId: Long,
    val uploaderName: String,
    val uploadDateTime: ZonedDateTime,
    val contents: String?,
)