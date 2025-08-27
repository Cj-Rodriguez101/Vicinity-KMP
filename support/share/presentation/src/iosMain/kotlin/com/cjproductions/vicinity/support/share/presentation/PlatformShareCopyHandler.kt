package com.cjproductions.vicinity.support.share.presentation

import com.cjproductions.vicinity.support.share.domain.LinkShareCopyHandler
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication


actual class PlatformShareCopyHandler: LinkShareCopyHandler {
  actual override fun shareOrCopyLink(link: String) {
    val activityController = UIActivityViewController(
      activityItems = listOf(link),
      applicationActivities = null
    )

    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController

    rootViewController?.presentViewController(
      activityController,
      animated = true,
      completion = null
    )
  }
}