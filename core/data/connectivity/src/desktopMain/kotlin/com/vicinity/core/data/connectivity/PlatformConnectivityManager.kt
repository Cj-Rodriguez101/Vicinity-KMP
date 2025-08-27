package com.vicinity.core.data.connectivity

import dev.jordond.connectivity.Connectivity

actual class PlatformConnectivityManager {
  actual val connectivity: Connectivity = Connectivity {
    autoStart = true
    port = 80
    timeoutMs = 5.seconds
  }
}