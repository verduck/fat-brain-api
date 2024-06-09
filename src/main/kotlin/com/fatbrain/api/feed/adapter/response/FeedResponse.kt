package com.fatbrain.api.feed.adapter.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fatbrain.api.feed.domain.Feed
import com.fatbrain.api.user.adapter.response.UserResponse
import java.time.Instant

data class FeedResponse(
  val id: Long,
  val title: String,
  val content: String,
  val feeder: UserResponse,
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  val createdAt: Instant,
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  val updatedAt: Instant,
) {
  companion object {
    fun fromDomain(domain: Feed): FeedResponse {
      return FeedResponse(
        id = domain.id!!,
        title = domain.title,
        content = domain.content,
        feeder = domain.feeder.run(UserResponse::fromDomain),
        createdAt = domain.auditMetaData.createdAt,
        updatedAt = domain.auditMetaData.updatedAt,
      )
    }
  }
}
