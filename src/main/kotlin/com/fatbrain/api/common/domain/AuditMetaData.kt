package com.fatbrain.api.common.domain

import jakarta.persistence.Embeddable
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Embeddable
data class AuditMetaData(
  @CreatedBy
  var createdBy: Long? = null,
  @CreatedDate
  var createdAt: Instant = Instant.MIN,
  @LastModifiedBy
  var updatedBy: Long? = null,
  @LastModifiedDate
  var updatedAt: Instant = Instant.MIN,
)
