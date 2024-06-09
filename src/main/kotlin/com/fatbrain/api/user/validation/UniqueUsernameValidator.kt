package com.fatbrain.api.user.validation

import com.fatbrain.api.user.service.UserService
import com.fatbrain.api.user.validation.constraint.UniqueUsername
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class UniqueUsernameValidator(
  private val userService: UserService,
): ConstraintValidator<UniqueUsername, String> {
  override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
    return !userService.existsUsername(value)
  }
}