package com.fatbrain.api.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class BearerAuthenticationToken(
  private val principal: Long?,
  private val credentials: String,
  private val authorities: Collection<GrantedAuthority>? = null
) : AbstractAuthenticationToken(authorities) {
  override fun getCredentials() = credentials
  override fun getPrincipal() = principal
}