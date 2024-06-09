package com.fatbrain.api.user.adapter

import com.fatbrain.api.feed.adapter.response.FeedResponse
import com.fatbrain.api.feed.service.FeedService
import com.fatbrain.api.user.adapter.request.CreateUserRequest
import com.fatbrain.api.user.adapter.response.MyTodayFeedCount
import com.fatbrain.api.user.adapter.response.UserResponse
import com.fatbrain.api.user.service.UserService
import com.fatbrain.api.user.service.command.CreateUserCommand
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.*
import java.util.TimeZone

@RestController
@RequestMapping("/v1/users")
@Tag(name = "사용자")
class UserController(
  private val userService: UserService,
  private val feedService: FeedService,
) {
  @PostMapping
  fun create(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
    val user = userService.createUser(CreateUserCommand(
      username = request.username,
      password = request.password,
      nickname = request.nickname,
    ))

    return ResponseEntity.ok(UserResponse.fromDomain(user))
  }

  @GetMapping("/me")
  @SecurityRequirement(name = "AccessToken")
  fun me(@AuthenticationPrincipal userId: Long): ResponseEntity<UserResponse> {
    val user = userService.findById(userId)
      ?: throw IllegalStateException("사용자 정보를 찾을 수 없습니다.")

    return ResponseEntity.ok(UserResponse.fromDomain(user))
  }

  @GetMapping("/me/feeds")
  @SecurityRequirement(name = "AccessToken")
  fun myFeeds(
    @AuthenticationPrincipal userId: Long,
    @RequestParam(defaultValue = "0") page: Int,
    @RequestParam(defaultValue = "20") size: Int,
  ): ResponseEntity<Page<FeedResponse>> {
    val response = feedService.findAllByFeederId(userId, PageRequest.of(page, size))
      .map(FeedResponse::fromDomain)

    return ResponseEntity.ok(response)
  }

  @GetMapping("/me/feeds/today/count")
  @SecurityRequirement(name = "AccessToken")
  fun myTodayFeedCount(
    @AuthenticationPrincipal userId: Long,
    @RequestParam(defaultValue = "Asia/Seoul") timezone: TimeZone,
  ): ResponseEntity<MyTodayFeedCount> {
    val zoneId = timezone.toZoneId()
    val today = LocalDate.now(zoneId)

    val count = feedService.countByFeederIdAndCreatedAtBetween(
      userId,
      today.atStartOfDay().atZone(zoneId).toInstant(),
      today.atTime(LocalTime.MAX).atZone(zoneId).toInstant(),
    )

    return ResponseEntity.ok(MyTodayFeedCount(count))
  }

}