package com.fatbrain.api.authentication

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.stereotype.Component

@Component
class BearerAuthenticationConverter : AuthenticationConverter {
  override fun convert(request: HttpServletRequest): BearerAuthenticationToken? {
    val header = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
    val token = resolveToken(header) ?: return null

    return BearerAuthenticationToken(null, token)
  }

  private fun resolveToken(header: String): String? {
    return if (header.startsWith("Bearer ")) {
      header.substring(7)
    } else {
      null
    }
  }
}