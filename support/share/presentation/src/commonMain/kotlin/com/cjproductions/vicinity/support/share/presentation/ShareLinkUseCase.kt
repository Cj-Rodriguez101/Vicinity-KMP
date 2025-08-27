package com.cjproductions.vicinity.support.share.presentation

import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.support.share.domain.LinkShareCopyHandler
import com.cjproductions.vicinity.support.tools.Platform
import com.cjproductions.vicinity.support.tools.PlatformType.ANDROID
import com.cjproductions.vicinity.support.tools.PlatformType.DESKTOP
import vicinity.support.share.presentation.generated.resources.Res
import vicinity.support.share.presentation.generated.resources.link_copied_to_clipboard
import kotlin.io.encoding.Base64

class ShareLinkUseCase(
  private val linkShareCopyHandler: LinkShareCopyHandler,
  private val snackBarController: SnackBarController,
) {
  suspend fun invoke(title: String) {
    val encodedTitle = title.encodeTitle()

    val encodedUrl = "$BASE_DEEP_LINK_URL$EVENT_DETAIL_PATH${encodedTitle}"
    linkShareCopyHandler.shareOrCopyLink(encodedUrl)
    if (Platform.type == DESKTOP) snackBarController.sendEvent(
      SnackBarUiEvent(UIText.StringResourceId(Res.string.link_copied_to_clipboard))
    )
  }

  private fun String.encodeTitle(): String {
    return Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT)
      .encode(this.encodeToByteArray())
  }
}

private const val DEFAULT_BASE_URL = "https://vicinityltd.app/"
private const val CUSTOM_BASE_URL = "vicinity://app/"
val BASE_DEEP_LINK_URL = DEFAULT_BASE_URL.takeIf { Platform.type == ANDROID } ?: CUSTOM_BASE_URL
const val EVENT_DETAIL_PATH = "event_detail/"