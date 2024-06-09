package com.fatbrain.api.feed.adapter

import com.fatbrain.api.feed.adapter.request.CreateFeedRequest
import com.fatbrain.api.feed.adapter.request.UpdateFeedRequest
import com.fatbrain.api.feed.adapter.response.FeedResponse
import com.fatbrain.api.feed.service.FeedService
import com.fatbrain.api.feed.service.command.CreateFeedCommand
import com.fatbrain.api.feed.service.command.DeleteFeedCommand
import com.fatbrain.api.feed.service.command.UpdateFeedCommand
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/feeds")
@Tag(name = "먹이")
@SecurityRequirement(name = "AccessToken")
class FeedController(
  private val feedService: FeedService,
) {
  @PostMapping
  fun createFeed(
    @AuthenticationPrincipal userId: Long,
    @Valid @RequestBody request: CreateFeedRequest,
  ): ResponseEntity<FeedResponse> {
    val feed = feedService.createFeed(CreateFeedCommand(request.title, request.content, userId))

    return ResponseEntity.ok(FeedResponse.fromDomain(feed))
  }

  @GetMapping("/{id}")
  fun findFeed(
    @AuthenticationPrincipal userId: Long,
    @PathVariable id: Long,
  ): ResponseEntity<FeedResponse> {
    val feed = feedService.findById(id)
      ?: throw IllegalStateException("먹이 정보를 찾을 수 없습니다.")

    return ResponseEntity.ok(FeedResponse.fromDomain(feed))
  }

  @PutMapping("/{id}")
  fun updatedFeed(
    @AuthenticationPrincipal userId: Long,
    @PathVariable id: Long,
    @Valid @RequestBody request: UpdateFeedRequest,
  ): ResponseEntity<FeedResponse> {
    val feed = feedService.updateFeed(UpdateFeedCommand(
      id,
      request.title,
      request.content,
      userId
    ))

    return ResponseEntity.ok(FeedResponse.fromDomain(feed))
  }

  @DeleteMapping("/{id}")
  fun deleteFeed(
    @AuthenticationPrincipal userId: Long,
    @PathVariable id: Long,
  ): ResponseEntity<Unit> {
    feedService.deleteFeed(DeleteFeedCommand(id, userId))

    return ResponseEntity.noContent().build()
  }
}