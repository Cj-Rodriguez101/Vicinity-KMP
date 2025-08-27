package com.cjproductions.vicinity.profile.data

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.compressImage

private const val MAX_SIZE = 360
private const val QUALITY = 40

suspend fun ByteArray.compressImage() = FileKit.compressImage(
  bytes = this,
  quality = QUALITY,
  maxWidth = MAX_SIZE,
  maxHeight = MAX_SIZE,
)