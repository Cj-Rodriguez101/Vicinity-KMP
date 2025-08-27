package com.cjproductions.vicinity.core.presentation.ui

import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.core.domain.util.DataError.Local.DISK_FULL
import com.cjproductions.vicinity.core.domain.util.DataError.Network.NO_INTERNET
import com.cjproductions.vicinity.core.domain.util.DataError.Network.PAYLOAD_TOO_LARGE
import com.cjproductions.vicinity.core.domain.util.DataError.Network.REQUEST_TIMEOUT
import com.cjproductions.vicinity.core.domain.util.DataError.Network.SERIALIZATION
import com.cjproductions.vicinity.core.domain.util.DataError.Network.SERVER_ERROR
import com.cjproductions.vicinity.core.domain.util.DataError.Network.TOO_MANY_REQUESTS
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText.StringResourceId
import vicinity.core.presentation.ui.generated.resources.Res.string
import vicinity.core.presentation.ui.generated.resources.error_disk_full
import vicinity.core.presentation.ui.generated.resources.error_no_internet
import vicinity.core.presentation.ui.generated.resources.error_payload_too_large
import vicinity.core.presentation.ui.generated.resources.error_request_timeout
import vicinity.core.presentation.ui.generated.resources.error_serialization
import vicinity.core.presentation.ui.generated.resources.error_server_error
import vicinity.core.presentation.ui.generated.resources.error_too_many_requests
import vicinity.core.presentation.ui.generated.resources.error_unknown

fun DataError.asUiText(): UIText {
  return when (this) {
    DISK_FULL -> StringResourceId(string.error_disk_full)

    REQUEST_TIMEOUT -> StringResourceId(string.error_request_timeout)

    TOO_MANY_REQUESTS -> StringResourceId(string.error_too_many_requests)

    NO_INTERNET -> StringResourceId(string.error_no_internet)

    PAYLOAD_TOO_LARGE -> StringResourceId(string.error_payload_too_large)

    SERVER_ERROR -> StringResourceId(string.error_server_error)

    SERIALIZATION -> StringResourceId(string.error_serialization)

    else -> StringResourceId(string.error_unknown)
  }
}