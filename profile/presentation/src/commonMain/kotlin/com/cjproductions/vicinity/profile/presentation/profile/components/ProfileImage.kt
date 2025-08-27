package com.cjproductions.vicinity.profile.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.cjproductions.vicinity.core.presentation.ui.components.ImageErrorContainer
import com.cjproductions.vicinity.core.presentation.ui.components.ImageLoadingContainer
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vicinity.profile.presentation.generated.resources.Res
import vicinity.profile.presentation.generated.resources.profile

@Composable
fun ProfileImage(
  avatarData: AvatarData,
  onImageClick: suspend () -> Unit,
) {
  val scope = rememberCoroutineScope()
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.size(100.dp)
      .background(
        color = MaterialTheme.colorScheme.outlineVariant,
        shape = CircleShape
      ).clip(CircleShape)
      .clickable { scope.launch { onImageClick() } }
  ) {
    when {
      avatarData.image == null -> {
        Text(
          text = avatarData.name.orEmpty().getInitialsFromString(),
          style = MaterialTheme.typography.headlineLarge,
        )
      }

      else -> {
        val imageModifier = Modifier.fillMaxSize().clip(CircleShape)
        SubcomposeAsyncImage(
          model = avatarData.image,
          contentDescription = stringResource(Res.string.profile),
          contentScale = ContentScale.Crop,
          loading = { ImageLoadingContainer(imageModifier) },
          error = { ImageErrorContainer(imageModifier) },
          modifier = imageModifier
        )
      }
    }
    if (avatarData.isLoading) {
      CircularProgressIndicator()
    }
  }
}

data class AvatarData(
  val name: String? = null,
  val isLoading: Boolean = false,
  val image: String? = null,
)

fun String.getInitialsFromString(): String {
  val words = this.trim().split(' ').filter { it.isNotEmpty() }
  if (words.isEmpty()) return ""

  val firstLetter = words[0].first().uppercaseChar()
  if (words.size == 1) return firstLetter.toString()

  val secondLetter = words[1].first().uppercaseChar()
  return "$firstLetter$secondLetter"
}
