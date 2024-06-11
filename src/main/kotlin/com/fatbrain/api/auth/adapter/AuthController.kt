package com.fatbrain.api.auth.adapter

import com.fatbrain.api.auth.adapter.request.TokenRequest
import com.fatbrain.api.auth.application.AuthService
import com.fatbrain.api.auth.domain.Token
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
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
  @Operation(summary = "인증 토큰 발행", description = "사용자 이름 / 비밀번호 또는 갱신 토큰으로 토큰을 발행합니다.")
  fun issueToken(
    @Valid @RequestBody
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = [
        Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = [
            ExampleObject(
              name = "사용자 이름 / 비밀번호으로 토큰 발행",
              value = """
                {
                  "grantType": "password",
                  "username": "string",
                  "password": "string"
                }
              """,
            ),
            ExampleObject(
              name = "갱신 토큰으로 토큰 발행",
              value = """
                {
                  "grantType": "refresh_token",
                  "refreshToken": "string"
                }
              """
            )
          ]
        )
      ]
    )
    requestBody: TokenRequest,
    request: HttpServletRequest,
    response: HttpServletResponse
  ): ResponseEntity<Token> {
    val token = authService.issueToken(requestBody.toCommand())

    return ResponseEntity.ok(token)
  }
}