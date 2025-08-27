package com.vicinity.core.data.connectivity

import dev.jordond.connectivity.Connectivity

actual class PlatformConnectivityManager {
  actual val connectivity: Connectivity = Connectivity { autoStart = true }
}
