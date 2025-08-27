package com.cjproductions.vicinity.profile.data

import Vicinity.profile.data.BuildConfig.PROJECT_ID
import Vicinity.profile.data.BuildConfig.STORAGE_URL
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.profile.domain.DomainFileStorage
import com.cjproductions.vicinity.profile.domain.FileStorageError
import com.cjproductions.vicinity.profile.domain.FileType
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.coroutines.cancellation.CancellationException

actual class PlatformStorage(
  private val httpClient: HttpClient,
): DomainFileStorage {

  actual override suspend fun storeFile(
    path: String,
    name: String,
    file: PlatformFile,
    fileType: FileType,
  ): Result<String?, FileStorageError> {
    return try {
      val fullPath = if (path.isNotEmpty()) "$path/$name" else name

      val encodedPath = fullPath.split("/")
        .joinToString(TWO_F) { java.net.URLEncoder.encode(it, UTF_8) }

      val uploadUrl = "$STORAGE_URL$PROJECT_ID$STORAGE_SUFFIX$encodedPath$ALT_MEDIA"

      val result = httpClient.post(uploadUrl) {
        headers { append(HttpHeaders.ContentType, fileType.type) }
        setBody(file.readBytes().compressImage())
      }

      if (result.status.isSuccess()) {
        val responseBody = result.bodyAsText()

        val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
        val bucket = jsonResponse[BUCKET]?.jsonPrimitive?.content
        val name = jsonResponse[NAME]?.jsonPrimitive?.content
        val downloadTokens = jsonResponse[DOWNLOAD_TOKENS]?.jsonPrimitive?.content

        if (bucket != null && name != null && downloadTokens != null) {
          val encodedName = java.net.URLEncoder.encode(name, UTF_8)
          val downloadUrl =
            "$STORAGE_URL$bucket/o/$encodedName$ALT_MEDIA_TOKEN$downloadTokens"

          Result.Success(downloadUrl)
        } else throw Exception("Failed to parse upload response - missing required fields")
      } else {
        println("Upload failed: ${result.status} - ${result.bodyAsText()}")
        throw Exception("Upload failed: ${result.status} - ${result.bodyAsText()}")
      }
    } catch (ex: Exception) {
      if (ex is CancellationException) throw ex
      println("Upload failed: ${ex.message}")
      Result.Error(FileStorageError.Unknown(ex.message ?: "Unknown error"))
    }
  }

  override suspend fun deleteFile(
    path: String,
    name: String,
  ): Result<Boolean, FileStorageError> {
    return try {
      val fullPath = if (path.isNotEmpty()) "$path/$name" else name

      val encodedPath = fullPath.split("/")
        .joinToString(TWO_F) { java.net.URLEncoder.encode(it, UTF_8) }

      val deleteUrl = "$STORAGE_URL$PROJECT_ID$STORAGE_SUFFIX$encodedPath"

      val result = httpClient.delete(deleteUrl)

      if (result.status.isSuccess()) {
        println("File deleted successfully: $fullPath")
        Result.Success(true)
      } else if (result.status == HttpStatusCode.NotFound) {
        println("File not found: $fullPath")
        Result.Success(false)
      } else {
        println("Delete failed: ${result.status} - ${result.bodyAsText()}")
        throw Exception("Delete failed: ${result.status} - ${result.bodyAsText()}")
      }
    } catch (ex: Exception) {
      if (ex is CancellationException) throw ex
      println("Delete failed: ${ex.message}")
      Result.Error(FileStorageError.Unknown(ex.message ?: "Unknown error"))
    }
  }


  companion object {
    private const val ALT_MEDIA = "?alt=media"
    private const val ALT_MEDIA_TOKEN = "$ALT_MEDIA&token="
    private const val STORAGE_SUFFIX = ".firebasestorage.app/o/"
    private const val UTF_8 = "UTF-8"
    private const val TWO_F = "%2F"
    private const val BUCKET = "bucket"
    private const val NAME = "name"
    private const val DOWNLOAD_TOKENS = "downloadTokens"
  }
}