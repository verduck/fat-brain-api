package com.fatbrain.api.auth.application.command

import com.fatbrain.api.auth.enums.GrantType

data class IssueTokenByRefreshTokenCommand(
  override val grantType: GrantType,
  val refreshToken: String,
): IssueTokenCommand