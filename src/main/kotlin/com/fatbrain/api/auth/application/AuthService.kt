package com.fatbrain.api.auth.application

import com.fatbrain.api.auth.application.command.IssueTokenByPasswordCommand
import com.fatbrain.api.auth.application.command.IssueTokenByRefreshTokenCommand
import com.fatbrain.api.auth.application.command.IssueTokenCommand
import com.fatbrain.api.auth.domain.Token
import com.fatbrain.api.config.properties.JwtProperties
import com.fatbrain.api.user.domain.User
import com.fatbrain.api.user.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional(readOnly = true)
class AuthService(
  private val userService: UserService,
  private val passwordEncoder: PasswordEncoder,
  private val accessJwtProperties: JwtProperties,
  private val accessJwtEncoder: JwtEncoder,
  @Qualifier("refreshJwtProperties")
  private val refreshJwtProperties: JwtProperties,
  @Qualifier("refreshJwtEncoder")
  private val refreshJwtEncoder: JwtEncoder,
  @Qualifier("refreshJwtDecoder")
  private val refreshJwtDecoder: JwtDecoder,
) {
  fun issueToken(command: IssueTokenCommand): Token {
    val user = when (command) {
      is IssueTokenByPasswordCommand -> getUserByPassword(command)
      is IssueTokenByRefreshTokenCommand -> getUserByRefreshToken(command)
      else -> throw BadCredentialsException("잘못된 유형입니다.")
    }

    val now = Instant.now()
    val accessJwt = generateJwtFromUser(user, false)
    val refreshJwt = generateJwtFromUser(user, true)

    return Token(
      accessToken = accessJwt.tokenValue,
      expiresIn = accessJwt.expiresAt?.epochSecond?.let { it - now.epochSecond } ?: 0,
      refreshToken = refreshJwt.tokenValue,
      refreshExpiresIn = refreshJwt.expiresAt?.epochSecond?.let { it - now.epochSecond } ?: 0,
    )
  }

  private fun getUserByPassword(command: IssueTokenByPasswordCommand): User {
    val user = userService.findByUsername(command.username)
      ?: throw BadCredentialsException("사용자 이름 또는 비밀번호가 일치하지 않습니다.")

    if (!passwordEncoder.matches(command.password, user.password)) {
      throw BadCredentialsException("사용자 이름 또는 비밀번호가 일치하지 않습니다.")
    }

    return user
  }

  private fun getUserByRefreshToken(command: IssueTokenByRefreshTokenCommand): User {
    val refreshJwt = refreshJwtDecoder.decode(command.refreshToken)

    val userId = refreshJwt.id.toLong()

    return userService.findById(userId)
      ?: throw BadCredentialsException("사용자 정보를 찾을 수 없습니다.")
  }

  private fun generateJwtFromUser(user: User, isRefresh: Boolean): Jwt {
    val now = Instant.now()

    val jwsHeader = JwsHeader.with { "HS256" }
      .header("type", "jwt")
      .build()

    val expires = if (isRefresh) {
      refreshJwtProperties.expiresIn
    } else {
      accessJwtProperties.expiresIn
    }

    val jwtClaimsSet = JwtClaimsSet.builder()
      .id(user.id.toString())
      .issuedAt(now)
      .expiresAt(now.plusSeconds(expires))
      .build()

    val jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet)

    val jwtEncoder = if (isRefresh) {
      refreshJwtEncoder
    } else {
      accessJwtEncoder
    }

    return jwtEncoder.encode(jwtEncoderParameters)
  }
}