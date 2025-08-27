package com.vicinity.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory() {

  fun build(): HttpClient {
    return HttpClient {
      install(ContentNegotiation) {
        json(
          json = Json {
            ignoreUnknownKeys = true
            isLenient = true
          }
        )
      }
      install(Logging) {
        logger = object: Logger {
          override fun log(message: String) {
            println(message)
          }
        }
        level = LogLevel.NONE
      }
      defaultRequest {
        contentType(ContentType.Application.Json)
      }
    }
  }
}