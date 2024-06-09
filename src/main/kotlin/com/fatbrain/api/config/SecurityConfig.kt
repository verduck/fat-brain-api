package com.fatbrain.api.config

import com.fatbrain.api.authentication.BearerAuthenticationEntryPoint
import com.fatbrain.api.authentication.BearerAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
  private val bearerAuthenticationFilter: BearerAuthenticationFilter,
) {
  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http.httpBasic { httpBasic -> httpBasic.disable() }
      .csrf { csrf -> csrf.disable() }
      .cors { }
      .formLogin { formLogin -> formLogin.disable() }
      .authorizeHttpRequests { authorize ->
        authorize.requestMatchers(
          AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name()),
          AntPathRequestMatcher("/error"),
          AntPathRequestMatcher("/v3/api-docs/**"),
          AntPathRequestMatcher("/swagger-ui/**"),
          AntPathRequestMatcher("/actuator/**"),
          AntPathRequestMatcher("/*/auth/**"),
          AntPathRequestMatcher("/*/users", HttpMethod.POST.name()),
        ).permitAll()
          .anyRequest().authenticated()
      }
      .addFilterBefore(bearerAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
      .exceptionHandling {
        it.authenticationEntryPoint(BearerAuthenticationEntryPoint())
      }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

    return http.build()
  }


  @Bean
  fun corsFilter(): CorsFilter {
    val config = CorsConfiguration()
    config.allowCredentials = true
    config.addAllowedOrigin("http://localhost:3000")
    config.addAllowedOrigin("https://jwirang.github.io")
    config.addAllowedMethod("*")
    config.addAllowedHeader("*")

    val source = UrlBasedCorsConfigurationSource().apply {
      registerCorsConfiguration("/**", config)
    }

    return CorsFilter(source)
  }

  @Bean
  fun passwordEncoder() = BCryptPasswordEncoder()
}