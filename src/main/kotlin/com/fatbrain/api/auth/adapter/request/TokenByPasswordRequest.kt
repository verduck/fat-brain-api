package com.fatbrain.api.auth.adapter.request

import com.fatbrain.api.auth.application.command.IssueTokenByPasswordCommand
import com.fatbrain.api.auth.application.command.IssueTokenCommand
import com.fatbrain.api.auth.enums.GrantType

data class TokenByPasswordRequest(
  override val grantType: GrantType,
  val username: String,
  val password: String,
): TokenRequest {
  override fun toCommand(): IssueTokenCommand {
    return IssueTokenByPasswordCommand(
      grantType = grantType,
      username = username,
      password = password,
    )
  }
}
