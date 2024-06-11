package com.fatbrain.api.auth.adapter.request

import com.fatbrain.api.auth.application.command.IssueTokenCommand
import com.fatbrain.api.auth.enums.GrantType

interface TokenRequest {
  val grantType: GrantType

  fun toCommand(): IssueTokenCommand
}
