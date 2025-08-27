package com.cjproductions.vicinity.support.share.presentation

import com.cjproductions.vicinity.support.share.domain.LinkShareCopyHandler
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual class PlatformShareCopyHandler: LinkShareCopyHandler {
  actual override fun shareOrCopyLink(link: String) {
    try {
      val selection = StringSelection(link)
      val clipboard = Toolkit.getDefaultToolkit().systemClipboard
      clipboard.setContents(selection, selection)
    } catch (e: Exception) {
      println("Failed to copy link to clipboard: ${e.message}")
    }
  }
}
