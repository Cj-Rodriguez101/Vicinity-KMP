package com.cjproductions.vicinity.support.permissions

expect class SupportPermissionController {
  suspend fun requestPermission(permission: PermissionType): PermissionState
  suspend fun getPermissionState(permission: PermissionType): PermissionState
  fun <T> bind(componentActivity: T)
}