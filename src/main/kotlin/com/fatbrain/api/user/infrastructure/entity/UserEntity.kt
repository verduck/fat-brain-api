package com.fatbrain.api.user.infrastructure.entity

import com.fatbrain.api.common.domain.AuditMetaData
import com.fatbrain.api.user.domain.User
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
data class UserEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long?,
  val username: String,
  val password: String,
  val nickname: String?,
  @Embedded
  val auditMetaData: AuditMetaData = AuditMetaData(),
) {
  companion object {
    fun fromDomain(user: User): UserEntity {
      return UserEntity(
        id = user.id,
        username = user.username,
        password = user.password,
        nickname = user.nickname,
      )
    }
  }
}
