package com.fatbrain.api.feed.persistence

import com.fatbrain.api.feed.domain.Feed
import com.fatbrain.api.feed.infrastructure.entity.FeedEntity
import com.fatbrain.api.feed.infrastructure.repostiroy.FeedRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FeedPersistence(
  private val feedRepository: FeedRepository,
) {
  fun save(feed: Feed): Feed {
    if (feed.id == null) {
      val feedEntity = FeedEntity.fromDomain(feed)

      return feedRepository.save(feedEntity).run(Feed::fromEntity)
    }

    val feedEntity = feedRepository.findByIdOrNull(feed.id)!!
    feedEntity.title = feed.title
    feedEntity.content = feed.content

    return Feed.fromEntity(feedEntity)
  }

  fun findAllByFeederId(feederId: Long, pageable: Pageable): Page<Feed> {
    return feedRepository.findAllByFeederId(feederId, pageable)
      .map(Feed::fromEntity)
  }

  fun findById(id: Long): Feed? {
    return feedRepository.findByIdOrNull(id)?.let(Feed::fromEntity)
  }

  fun countByFeederIdAndCreatedAt(feederId: Long, startCreatedAt: Instant, endCreatedAt: Instant): Long {
    return feedRepository.countByFeederIdAndAuditMetaDataCreatedAtBetween(feederId, startCreatedAt, endCreatedAt)
  }

  fun delete(feed: Feed) {
    val feedEntity = FeedEntity.fromDomain(feed)

    return feedRepository.delete(feedEntity)
  }
}