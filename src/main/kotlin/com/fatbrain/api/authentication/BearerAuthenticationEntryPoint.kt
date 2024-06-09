package com.fatbrain.api.authentication

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fatbrain.api.common.response.MessageResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class BearerAuthenticationEntryPoint : AuthenticationEntryPoint {
  private val objectMapper = jacksonObjectMapper()

  override fun commence(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authException: AuthenticationException,
  ) {
    response.status = HttpStatus.UNAUTHORIZED.value()
    response.contentType = MediaType.APPLICATION_JSON.toString()

    response.outputStream.write(objectMapper.writeValueAsBytes(MessageResponse(authException.localizedMessage)))
  }
}