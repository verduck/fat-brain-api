package com.fatbrain.api.feed.adapter.request

import jakarta.validation.constraints.NotBlank

data class CreateFeedRequest(
  @field:NotBlank(message = "제목은 공백일 수 없습니다.")
  val title: String,
  val content: String,
)
