package com.fatbrain.api.auth.adapter.request

import com.fatbrain.api.auth.enums.GrantType

data class TokenRequest(
  val grantType: GrantType,
  val username: String?,
  val password: String?,
  val refreshToken: String?,
)
