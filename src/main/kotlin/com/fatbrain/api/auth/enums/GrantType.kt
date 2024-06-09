package com.fatbrain.api.auth.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class GrantType(
  @JsonValue val value: String,
) {
  PASSWORD("password"),
  REFRESH_TOKEN("refresh_token");
}