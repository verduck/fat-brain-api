package com.fatbrain.api.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
  var secret: String = "",
  var expiresIn: Long = 0,
)
