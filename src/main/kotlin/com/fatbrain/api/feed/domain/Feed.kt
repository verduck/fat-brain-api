package com.fatbrain.api.feed.domain

import com.fatbrain.api.common.domain.AuditMetaData
import com.fatbrain.api.feed.infrastructure.entity.FeedEntity
import com.fatbrain.api.user.domain.User

data class Feed(
  val id: Long? = null,
  val title: String,
  val content: String,
  val feeder: User,
  val auditMetaData: AuditMetaData = AuditMetaData(),
) {
  companion object {
    fun fromEntity(entity: FeedEntity): Feed {
      return Feed(
        id = entity.id,
        title = entity.title,
        content = entity.content,
        feeder = entity.feeder.run(User::fromEntity),
        auditMetaData = entity.auditMetaData,
      )
    }
  }
}
