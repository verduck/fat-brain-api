package com.fatbrain.api.feed.service.command

data class UpdateFeedCommand(
  val id: Long,
  val title: String,
  val content: String,
  val feederId: Long,
)
