package com.fatbrain.api.feed.infrastructure.entity

import com.fatbrain.api.common.domain.AuditMetaData
import com.fatbrain.api.feed.domain.Feed
import com.fatbrain.api.user.infrastructure.entity.UserEntity
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "feeds")
@EntityListeners(AuditingEntityListener::class)
data class FeedEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
  var title: String,
  var content: String,
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "feeder_id")
  val feeder: UserEntity,
  val auditMetaData: AuditMetaData = AuditMetaData(),
) {
  companion object {
    fun fromDomain(feed: Feed): FeedEntity {
      return FeedEntity(
        id = feed.id,
        title = feed.title,
        content = feed.content,
        feeder = feed.feeder.run(UserEntity::fromDomain)
      )
    }
  }
}
