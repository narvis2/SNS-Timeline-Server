package com.narvi.timelineserver.timeline.follow

import com.narvi.timelineserver.common.utils.JsonUtil
import org.springframework.stereotype.Component

@Component
class FollowerListener(
    private val jsonUtil: JsonUtil,
    private val followerStore: FollowerStore,
) {


    fun listen(message: String) {

    }
}