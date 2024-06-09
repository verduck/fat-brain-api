package com.fatbrain.api.user.adapter.response

import com.fatbrain.api.user.domain.User

data class UserResponse(
  val id: Long,
  val username: String,
  val nickname: String?,
) {
  companion object {
    fun fromDomain(user: User): UserResponse {
      return UserResponse(
        id = user.id!!,
        username = user.username,
        nickname = user.nickname,
      )
    }
  }
}
