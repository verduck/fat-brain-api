package com.fatbrain.api.auth.adapter.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fatbrain.api.auth.adapter.request.TokenByPasswordRequest
import com.fatbrain.api.auth.adapter.request.TokenByRefreshTokenRequest
import com.fatbrain.api.auth.adapter.request.TokenRequest
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class TokenRequestDeserializer : JsonDeserializer<TokenRequest>() {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): TokenRequest {
    val jsonNode = ctxt.readTree(p)

    val grantType = jsonNode["grantType"]?.textValue()
      ?: throw JsonMappingException.wrapWithPath(
        MismatchedInputException.from(ctxt, "value failed for JSON property grantType due to missing (therefore NULL) value for creator parameter grantType which is a non-nullable type"),
        TokenRequest::class.java,
        "grantType"
      )

    return when (grantType) {
      "password" -> ctxt.readTreeAsValue(jsonNode, TokenByPasswordRequest::class.java)
      "refresh_token" -> ctxt.readTreeAsValue(jsonNode, TokenByRefreshTokenRequest::class.java)
      else -> throw IllegalArgumentException("invalid request token grant type")
    }
  }
}