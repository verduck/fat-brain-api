package com.fatbrain.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import kotlin.reflect.safeCast

@Configuration
@EnableJpaAuditing
class JpaConfig {
  @Bean
  fun auditorAware(): AuditorAware<Long> {
    return AuditorAware<Long> {
      Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(Authentication::getPrincipal)
        .map(Long::class::safeCast)
    }
  }
}