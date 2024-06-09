package com.fatbrain.api.common.advice

import com.fasterxml.jackson.databind.JsonMappingException
import com.fatbrain.api.common.response.MessageResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.IllegalArgumentException

@RestControllerAdvice
class ResponseEntityExceptionAdvice : ResponseEntityExceptionHandler() {
  override fun handleMethodArgumentNotValid(
    ex: MethodArgumentNotValidException,
    headers: HttpHeaders,
    status: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any>? {
    val detail = ex.bindingResult.allErrors.associate { error ->
      val fieldName = if (error is FieldError) {
        error.field
      } else {
        error.arguments?.last()?.toString()
      }
      val errorMessage = error.defaultMessage

      return@associate fieldName to errorMessage
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(detail)
  }

  override fun handleHttpMessageNotReadable(
    ex: HttpMessageNotReadableException,
    headers: HttpHeaders,
    status: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any>? {
    val detail = ex.cause?.let {
      when(val cause = it) {
        is JsonMappingException -> {
          val regex = "\\[\\\"([^\\\"\\]]+)".toRegex()
          val fieldName = regex.find(cause.pathReference)?.value?.substring(2)
          fieldName to "must not be null"
        }
        else -> throw ex
      }
    }?.let(::mapOf)

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(detail)
  }

  @ExceptionHandler(ConstraintViolationException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<Any> {
    val detail = ex.constraintViolations.associate { error ->
      val fieldName = error.propertyPath.last().toString()
      val errorMessage = error.message

      return@associate fieldName to errorMessage
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(detail)
  }

  @ExceptionHandler(BadCredentialsException::class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<MessageResponse> {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
      .body(MessageResponse(ex.message))
  }

  @ExceptionHandler(IllegalArgumentException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<MessageResponse> {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(MessageResponse(ex.message))
  }

  @ExceptionHandler(IllegalStateException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<MessageResponse> {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(MessageResponse(ex.message))
  }
}