package com.narvi.timelineserver.timeline.feed

import com.narvi.timelineserver.common.utils.JsonUtil
import org.springframework.stereotype.Component

@Component
class FeedListener(
    private val jsonUtil: JsonUtil,
    private val feedStore: FeedStore,
) {

    fun listen(message: String) {

    }
}