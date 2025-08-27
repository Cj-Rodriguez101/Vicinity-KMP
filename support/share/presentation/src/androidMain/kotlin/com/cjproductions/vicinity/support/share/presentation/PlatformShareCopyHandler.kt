package com.cjproductions.vicinity.support.share.presentation

import android.app.Application
import android.content.Intent
import com.cjproductions.vicinity.support.share.domain.LinkShareCopyHandler

actual class PlatformShareCopyHandler(
  val app: Application,
): LinkShareCopyHandler {
  actual override fun shareOrCopyLink(link: String) {
    val sendIntent = Intent(Intent.ACTION_SEND)
      .setType("text/plain")
      .putExtra(Intent.EXTRA_TEXT, link)
      .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val shareIntent = Intent.createChooser(sendIntent, null)
    shareIntent.putExtra(Intent.EXTRA_TITLE, "Introducing content previews")
    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    app.startActivity(shareIntent)
  }
}