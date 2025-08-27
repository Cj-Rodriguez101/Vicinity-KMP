package com.cjproductions.vicinity.support.share.presentation

import com.cjproductions.vicinity.support.share.domain.LinkShareCopyHandler

expect class PlatformShareCopyHandler: LinkShareCopyHandler {
  override fun shareOrCopyLink(link: String)
}