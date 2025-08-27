package com.vicinity.core.data.connectivity

import dev.jordond.connectivity.Connectivity

expect class PlatformConnectivityManager() {
  val connectivity: Connectivity
}