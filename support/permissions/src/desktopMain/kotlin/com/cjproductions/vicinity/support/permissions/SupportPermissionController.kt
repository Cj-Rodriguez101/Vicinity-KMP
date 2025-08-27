package com.cjproductions.vicinity.support.permissions

actual class SupportPermissionController() {
  actual suspend fun requestPermission(permission: PermissionType) = PermissionState.DENIED
  actual suspend fun getPermissionState(permission: PermissionType) = PermissionState.DENIED
  actual fun <T> bind(componentActivity: T) = Unit
}