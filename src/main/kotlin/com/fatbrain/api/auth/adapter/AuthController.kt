package com.fatbrain.api.auth.adapter

import com.fatbrain.api.auth.adapter.request.TokenRequest
import com.fatbrain.api.auth.application.AuthService
import com.fatbrain.api.auth.application.command.IssueTokenByPasswordCommand
import com.fatbrain.api.auth.application.command.IssueTokenByRefreshTokenCommand
import com.fatbrain.api.auth.domain.Token
import com.fatbrain.api.auth.enums.GrantType
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/auth")
@Tag(name = "인증")
class AuthController(
  private val authService: AuthService,
) {
  @PostMapping("/token")
  fun issueToken(
    @RequestBody
    requestBody: TokenRequest,
    request: HttpServletRequest,
    response: HttpServletResponse
  ): ResponseEntity<Token> {

    val command = when (requestBody.grantType) {
      GrantType.PASSWORD -> IssueTokenByPasswordCommand(
        requestBody.grantType,
        requestBody.username!!,
        requestBody.password!!,
      )
      GrantType.REFRESH_TOKEN -> IssueTokenByRefreshTokenCommand(
        requestBody.grantType,
        requestBody.refreshToken!!
      )
    }

    val token = authService.issueToken(command)

    return ResponseEntity.ok(token)
  }
}