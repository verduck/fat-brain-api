package com.fatbrain.api.user.persistence

import com.fatbrain.api.user.domain.User
import com.fatbrain.api.user.infrastructure.entity.UserEntity
import com.fatbrain.api.user.infrastructure.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserPersistence(
  private val userRepository: UserRepository,
) {
  fun findById(id: Long): User? {
    return userRepository.findByIdOrNull(id)?.let(User::fromEntity)
  }
  fun findByUsername(username: String): User? {
    return userRepository.findByUsername(username)?.let(User::fromEntity)
  }

  fun existsByUsername(username: String): Boolean {
    return userRepository.existsByUsername(username)
  }

  fun save(user: User): User {
    val userEntity = UserEntity.fromDomain(user)

    if (userEntity.id == null) {
      return userRepository.save(userEntity).run(User::fromEntity)
    }

    return userRepository.save(userEntity).run(User::fromEntity)
  }
}