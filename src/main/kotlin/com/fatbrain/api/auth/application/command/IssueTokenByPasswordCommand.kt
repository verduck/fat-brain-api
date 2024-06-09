package com.fatbrain.api.auth.application.command

import com.fatbrain.api.auth.enums.GrantType

data class IssueTokenByPasswordCommand(
  override val grantType: GrantType,
  val username: String,
  val password: String,
): IssueTokenCommand
