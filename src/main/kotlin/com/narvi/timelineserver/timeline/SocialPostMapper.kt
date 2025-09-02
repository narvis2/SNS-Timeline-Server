package com.narvi.timelineserver.timeline

import com.narvi.timelineserver.timeline.feed.FeedInfo
import org.springframework.stereotype.Component

@Component
class SocialPostMapper {

    fun feedInfoToSocialPostWithLike(
        post: FeedInfo,
        likes: Map<Long, Long>,
    ): SocialPost = SocialPost(
        feedId = post.feedId,
        imageId = post.imageId,
        uploaderName = post.uploaderName,
        uploaderId = post.uploaderId,
        uploadDateTime = post.uploadDateTime,
        contents = post.contents,
        likes = likes.getOrDefault(post.feedId, 0L)
    )
}