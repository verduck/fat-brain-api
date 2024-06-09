package com.fatbrain.api.feed.service

import com.fatbrain.api.feed.domain.Feed
import com.fatbrain.api.feed.persistence.FeedPersistence
import com.fatbrain.api.feed.service.command.CreateFeedCommand
import com.fatbrain.api.feed.service.command.DeleteFeedCommand
import com.fatbrain.api.feed.service.command.UpdateFeedCommand
import com.fatbrain.api.user.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional(readOnly = true)
class FeedService(
  private val feedPersistence: FeedPersistence,
  private val userService: UserService,
) {
  fun findAllByFeederId(feederId: Long, pageable: Pageable): Page<Feed> {
    return feedPersistence.findAllByFeederId(feederId, pageable)
  }

  fun findById(id: Long): Feed? {
    return feedPersistence.findById(id)
  }

  fun countByFeederIdAndCreatedAtBetween(feederId: Long, startCreatedAt: Instant, endCreatedAt: Instant): Long  {
    return feedPersistence.countByFeederIdAndCreatedAt(feederId, startCreatedAt, endCreatedAt)
  }

  @Transactional
  fun createFeed(command: CreateFeedCommand): Feed {
    val user = userService.findById(command.feederId)
      ?: throw IllegalStateException("사용자 정보를 찾을 수 없습니다.")

    val feed = Feed(
      title = command.title,
      content = command.content,
      feeder = user,
    )

    return save(feed)
  }

  @Transactional
  fun updateFeed(command: UpdateFeedCommand): Feed {
    val feed = feedPersistence.findById(command.id)
      ?: throw IllegalStateException("먹이 정보를 찾을 수 없습니다.")

    if (feed.feeder.id != command.feederId) {
      throw IllegalArgumentException("나의 먹이만 수정할 수 있습니다.")
    }

    return feed.copy(title = command.title, content = command.content)
      .run(this::save)
  }

  @Transactional
  fun deleteFeed(command: DeleteFeedCommand) {
    val feed = feedPersistence.findById(command.id)
      ?: throw IllegalStateException("먹이 정보를 찾을 수 없습니다.")

    if (feed.feeder.id != command.feederId) {
      throw IllegalArgumentException("나의 먹이만 수정할 수 있습니다.")
    }

    return feedPersistence.delete(feed)
  }

  @Transactional
  fun save(feed: Feed): Feed {
    return feedPersistence.save(feed)
  }
}