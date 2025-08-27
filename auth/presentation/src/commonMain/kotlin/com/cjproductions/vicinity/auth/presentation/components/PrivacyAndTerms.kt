package com.cjproductions.vicinity.auth.presentation.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import org.jetbrains.compose.resources.stringResource
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.and
import vicinity.auth.presentation.generated.resources.privacy
import vicinity.auth.presentation.generated.resources.terms_and_conditions
import vicinity.core.presentation.ui.generated.resources.privacy_url
import vicinity.core.presentation.ui.generated.resources.terms_url
import vicinity.core.presentation.ui.generated.resources.Res as CoreRes

@Composable
fun PrivacyAndTerms(modifier: Modifier = Modifier) {
  val annotatedString = buildAnnotatedString {
    withLink(LinkAnnotation.Url(url = stringResource(CoreRes.string.privacy_url))) {
      withStyle(
        SpanStyle(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
        )
      ) { append(stringResource(Res.string.privacy)) }
    }

    append(" ${stringResource(Res.string.and)} ")

    withLink(LinkAnnotation.Url(url = stringResource(CoreRes.string.terms_url))) {
      withStyle(
        SpanStyle(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
        )
      ) { append(stringResource(Res.string.terms_and_conditions)) }
    }
  }

  Text(
    text = annotatedString,
    textAlign = TextAlign.Center,
    modifier = modifier,
  )
}

@Preview
@Composable
fun PrivacyAndTermsPreview() {
  VicinityTheme {
    PrivacyAndTerms()
  }
}