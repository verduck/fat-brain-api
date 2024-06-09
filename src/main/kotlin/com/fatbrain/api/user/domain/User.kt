package com.fatbrain.api.user.domain

import com.fatbrain.api.user.infrastructure.entity.UserEntity

data class User(
  val id: Long?,
  val username: String,
  val password: String,
  val nickname: String?,
) {
  companion object {
    fun fromEntity(userEntity: UserEntity): User {
      return User(
        id = userEntity.id,
        username = userEntity.username,
        password = userEntity.password,
        nickname = userEntity.nickname,
      )
    }
  }
}
