package com.cjproductions.vicinity.support.permissions

import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.ios.PermissionsControllerProtocol
import dev.icerock.moko.permissions.location.COARSE_LOCATION
import dev.icerock.moko.permissions.location.LOCATION

actual class SupportPermissionController(
  private val permissionsController: PermissionsControllerProtocol
) {
  actual suspend fun requestPermission(permission: PermissionType): PermissionState {
    return try {
      permissionsController.providePermission(permission.toMokoPermission())
      PermissionState.GRANTED
    } catch (deniedAlways: DeniedAlwaysException) {
      PermissionState.DENIED_ALWAYS
    } catch (denied: DeniedException) {
      PermissionState.DENIED
    } catch (e: RequestCanceledException) {
      PermissionState.REQUEST_CANCELED
    }
  }

  actual suspend fun getPermissionState(permission: PermissionType): PermissionState {
    val mokoPermissionState =
      permissionsController.getPermissionState(permission.toMokoPermission())
    return when (mokoPermissionState) {
      dev.icerock.moko.permissions.PermissionState.Granted -> PermissionState.GRANTED
      dev.icerock.moko.permissions.PermissionState.Denied -> PermissionState.DENIED
      dev.icerock.moko.permissions.PermissionState.NotDetermined -> PermissionState.NOT_DETERMINED
      dev.icerock.moko.permissions.PermissionState.NotGranted -> PermissionState.NOT_GRANTED
      dev.icerock.moko.permissions.PermissionState.DeniedAlways -> PermissionState.DENIED_ALWAYS
    }
  }

  actual fun <T> bind(componentActivity: T) = Unit
}

private fun PermissionType.toMokoPermission(): Permission {
  return when (this) {
    PermissionType.FINE_LOCATION -> Permission.LOCATION
    PermissionType.COARSE_LOCATION -> Permission.COARSE_LOCATION
  }
}