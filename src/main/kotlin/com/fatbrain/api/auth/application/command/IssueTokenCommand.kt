package com.fatbrain.api.auth.application.command

import com.fatbrain.api.auth.enums.GrantType

interface IssueTokenCommand {
  val grantType: GrantType
}