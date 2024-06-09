package com.fatbrain.api.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class OpenAPIConfig {
  @Bean
  fun openAPI(): OpenAPI {
    val info = Info()
      .title("Fat Bran API")

    val bearerAuth = SecurityScheme()
      .type(SecurityScheme.Type.HTTP)
      .scheme("bearer")
      .bearerFormat("Authorization")
      .`in`(SecurityScheme.In.HEADER)
      .name(HttpHeaders.AUTHORIZATION)

    return OpenAPI()
      .info(info)
      .components(Components().addSecuritySchemes("AccessToken", bearerAuth))
  }
}