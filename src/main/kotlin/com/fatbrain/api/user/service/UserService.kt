package com.fatbrain.api.user.service

import com.fatbrain.api.user.domain.User
import com.fatbrain.api.user.persistence.UserPersistence
import com.fatbrain.api.user.service.command.CreateUserCommand
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
  private val userPersistence: UserPersistence,
  private val passwordEncoder: PasswordEncoder,
) {
  @Transactional
  fun createUser(command: CreateUserCommand): User {
    val user = User(
      id = null,
      username = command.username,
      password = passwordEncoder.encode(command.password),
      nickname = command.nickname,
    )

    return save(user)
  }

  fun findById(id: Long): User? {
    return userPersistence.findById(id)
  }

  fun findByUsername(username: String): User? {
    return userPersistence.findByUsername(username)
  }

  fun existsUsername(username: String): Boolean {
    return userPersistence.existsByUsername(username)
  }

  @Transactional
  fun save(user: User): User {
    return userPersistence.save(user)
  }
}