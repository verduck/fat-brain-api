package com.fatbrain.api.auth.domain

data class Token(
  val tokenType: String = "Bearer",
  val accessToken: String,
  val refreshToken: String,
  val expiresIn: Long,
  val refreshExpiresIn: Long,
)
