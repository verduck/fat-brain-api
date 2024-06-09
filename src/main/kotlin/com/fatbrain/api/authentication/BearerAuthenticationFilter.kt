package com.fatbrain.api.authentication

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fatbrain.api.common.response.MessageResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class BearerAuthenticationFilter(
  bearerAuthenticationManager: BearerAuthenticationManager,
  bearerAuthenticationConverter: BearerAuthenticationConverter,
) : AuthenticationFilter(bearerAuthenticationManager, bearerAuthenticationConverter) {
  private val objectMapper = jacksonObjectMapper()

  init {
    successHandler = AuthenticationSuccessHandler { request, response, authentication ->
    }

    failureHandler = AuthenticationFailureHandler { request, response, exception ->
      response.status = HttpStatus.UNAUTHORIZED.value()
      response.contentType = MediaType.APPLICATION_JSON.toString()

      val os = response.outputStream
      os.write(objectMapper.writeValueAsBytes(MessageResponse(exception.localizedMessage)))
    }
  }
}