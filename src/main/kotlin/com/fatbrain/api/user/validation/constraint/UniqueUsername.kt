package com.fatbrain.api.user.validation.constraint

import com.fatbrain.api.user.validation.UniqueUsernameValidator
import jakarta.validation.Constraint
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueUsernameValidator::class])
annotation class UniqueUsername(
  val message: String = "이미 사용 중인 사용자 이름입니다.",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<*>> = []
)
