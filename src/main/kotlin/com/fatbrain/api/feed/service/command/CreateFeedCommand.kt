package com.fatbrain.api.feed.service.command

data class CreateFeedCommand(
  val title: String,
  val content: String,
  val feederId: Long,
)
