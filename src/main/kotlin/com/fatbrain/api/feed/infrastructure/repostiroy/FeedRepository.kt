package com.fatbrain.api.feed.infrastructure.repostiroy

import com.fatbrain.api.feed.infrastructure.entity.FeedEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface FeedRepository: JpaRepository<FeedEntity, Long> {
  fun findAllByFeederId(feederId: Long, pageable: Pageable): Page<FeedEntity>
  fun countByFeederIdAndAuditMetaDataCreatedAtBetween(feederId: Long, startCreatedAt: Instant, endCreatedAt: Instant): Long
}