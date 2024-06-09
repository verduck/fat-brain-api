package com.fatbrain.api.user.service.command

data class CreateUserCommand(
  val username: String,
  val password: String,
  val nickname: String?,
)
