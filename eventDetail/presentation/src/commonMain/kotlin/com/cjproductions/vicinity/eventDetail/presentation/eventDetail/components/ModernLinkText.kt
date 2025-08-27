package com.cjproductions.vicinity.eventDetail.presentation.eventDetail.components

import androidx.compose.material.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalTextApi::class)
@Composable
fun ModernLinkText(
  text: String,
  modifier: Modifier = Modifier,
  style: TextStyle = LocalTextStyle.current,
  linkColor: Color = Color.Blue,
) {
  val urlPattern =
    Regex("https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+|www\\.[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+")

  val annotatedString = buildAnnotatedString {
    append(text)

    urlPattern.findAll(text).forEach { matchResult ->
      val url = matchResult.value
      val startIndex = matchResult.range.first
      val endIndex = matchResult.range.last + 1

      val fullUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "https://$url"
      } else url

      addStyle(
        style = SpanStyle(
          color = linkColor,
          textDecoration = TextDecoration.Underline
        ),
        start = startIndex,
        end = endIndex
      )

      addLink(
        url = LinkAnnotation.Url(fullUrl),
        start = startIndex,
        end = endIndex
      )
    }
  }

  Text(
    text = annotatedString,
    style = style,
    modifier = modifier
  )
}