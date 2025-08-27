package com.cjproductions.vicinity.support.tools

fun String.normalizeText(): String {
  return this
    .lowercase()
    .replace(Regex("[^a-z0-9\\s]"), "")
    .replace(Regex("\\s+"), "-")
    .trim('-')
}