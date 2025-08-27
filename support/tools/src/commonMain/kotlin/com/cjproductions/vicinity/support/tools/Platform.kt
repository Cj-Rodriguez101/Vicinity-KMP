package com.cjproductions.vicinity.support.tools

enum class PlatformType {
  ANDROID,
  IOS,
  DESKTOP,
}

expect object Platform {
  val type: PlatformType
}