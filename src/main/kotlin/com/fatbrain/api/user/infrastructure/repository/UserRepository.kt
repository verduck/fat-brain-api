package com.fatbrain.api.user.infrastructure.repository

import com.fatbrain.api.user.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<UserEntity, Long> {
  fun findByUsername(username: String): UserEntity?
  fun existsByUsername(username: String): Boolean
}