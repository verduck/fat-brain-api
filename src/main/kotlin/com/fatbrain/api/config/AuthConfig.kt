package com.fatbrain.api.config

import com.fatbrain.api.config.properties.JwtProperties
import com.nimbusds.jose.jwk.source.ImmutableSecret
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import javax.crypto.spec.SecretKeySpec

@Configuration
class AuthConfig {
  @Bean
  @Primary
  @ConfigurationProperties(prefix = "auth.access")
  fun accessJwtProperties(): JwtProperties {
    return JwtProperties()
  }
  @Bean
  @Primary
  fun accessJwkSource(): JWKSource<SecurityContext> {
    val secretKey = SecretKeySpec(accessJwtProperties().secret.toByteArray(), "HmacSHA256")
    return ImmutableSecret(secretKey)
  }

  @Bean
  @Primary
  fun accessJwtEncoder(): JwtEncoder {
    return NimbusJwtEncoder(accessJwkSource())
  }

  @Bean
  @Primary
  fun accessJwtDecoder(): JwtDecoder {
    val secretKey = SecretKeySpec(accessJwtProperties().secret.toByteArray(), "HmacSHA256")

    return NimbusJwtDecoder.withSecretKey(secretKey).build()
  }

  @Bean
  @ConfigurationProperties(prefix = "auth.refresh")
  fun refreshJwtProperties(): JwtProperties {
    return JwtProperties()
  }
  @Bean
  fun refreshJwkSource(): JWKSource<SecurityContext> {
    val secretKey = SecretKeySpec(refreshJwtProperties().secret.toByteArray(), "HmacSHA256")
    return ImmutableSecret(secretKey)
  }

  @Bean
  fun refreshJwtEncoder(): JwtEncoder {
    return NimbusJwtEncoder(refreshJwkSource())
  }

  @Bean
  fun refreshJwtDecoder(): JwtDecoder {
    val secretKey = SecretKeySpec(refreshJwtProperties().secret.toByteArray(), "HmacSHA256")

    return NimbusJwtDecoder.withSecretKey(secretKey).build()
  }
}