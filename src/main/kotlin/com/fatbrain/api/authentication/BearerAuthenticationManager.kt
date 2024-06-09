package com.fatbrain.api.authentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.stereotype.Component

@Component
class BearerAuthenticationManager(
  private val jwtDecoder: JwtDecoder,
) : AuthenticationManager {
  @Throws(BadCredentialsException::class)
  override fun authenticate(authentication: Authentication): Authentication {
    val jwt = jwtDecoder.decode(authentication.credentials.toString())

    return BearerAuthenticationToken(
      jwt.id.toLong(),
      authentication.credentials.toString(),
      listOf(),
    ).apply {
      isAuthenticated = true
      SecurityContextHolder.getContext().authentication = this
    }
  }
}