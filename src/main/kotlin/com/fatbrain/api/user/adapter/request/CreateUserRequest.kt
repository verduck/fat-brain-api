package com.fatbrain.api.user.adapter.request

import com.fatbrain.api.user.validation.constraint.UniqueUsername
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateUserRequest(
  @field:NotBlank(message = "사용자 이름은 공백일 수 없습니다.")
  @field:UniqueUsername
  val username: String,
  @field:NotBlank(message = "비밀번호는 공백일 수 없습니다.")
  @field:Min(value = 8, message = "비밀번호는 8자 이상이어야 합니다.")
  val password: String,
  val nickname: String?
)
