package com.fatbrain.api.auth.adapter.request

import com.fatbrain.api.auth.application.command.IssueTokenByRefreshTokenCommand
import com.fatbrain.api.auth.application.command.IssueTokenCommand
import com.fatbrain.api.auth.enums.GrantType

data class TokenByRefreshTokenRequest(
  override val grantType: GrantType,
  val refreshToken: String,
): TokenRequest {
  override fun toCommand(): IssueTokenCommand {
    return IssueTokenByRefreshTokenCommand(
      grantType = grantType,
      refreshToken = refreshToken,
    )
  }
}
