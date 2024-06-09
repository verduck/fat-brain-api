package com.fatbrain.api.feed.service.command

data class DeleteFeedCommand(
  val id: Long,
  val feederId: Long,
)
