package com.cjproductions.vicinity.eventDetail.presentation.eventDates

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.BOTTOM_SHEET_HEIGHT
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCard
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusVerticalGrid
import com.cjproductions.vicinity.core.presentation.ui.error.ErrorCard
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.eventDetail.presentation.eventDates.EventDateAction.OnRetryClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDates.EventDateState.Error
import com.cjproductions.vicinity.eventDetail.presentation.eventDates.EventDateState.Loaded
import com.cjproductions.vicinity.eventDetail.presentation.eventDates.EventDateState.Loading
import com.cjproductions.vicinity.support.tools.toDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.eventdetail.presentation.generated.resources.Res
import vicinity.eventdetail.presentation.generated.resources.events_on
import vicinity.eventdetail.presentation.generated.resources.no_events_found

@Composable
fun EventDateScreenRoot(
  viewModel: EventDateViewModel = koinViewModel(),
  onDismiss: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  EventDateScreen(
    uiState = uiState,
    onAction = viewModel::onAction,
    onDismiss = onDismiss,
  )
}

@Composable
private fun EventDateScreen(
  uiState: EventDateState,
  onAction: (EventDateAction) -> Unit,
  onDismiss: () -> Unit,
) {
  VicinityTheme {
    Scaffold(modifier = Modifier.height(BOTTOM_SHEET_HEIGHT)) { _ ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = GlobalPaddingAndSize.Medium.dp)
          .navigationBarsPadding(),
      ) {
        val uriHandler = LocalUriHandler.current
        when (uiState) {
          is Error -> ErrorCard(
            modifier = Modifier.align(Alignment.Center),
            onRetry = { onAction(OnRetryClicked) }
          )

          Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

          is Loaded -> {
            if (uiState.eventDates.isEmpty()) {
              Text(
                text = stringResource(Res.string.no_events_found),
                modifier = Modifier.align(Alignment.Center)
              )
            } else {
              RadiusVerticalGrid(
                contentPadding = PaddingValues(vertical = GlobalPaddingAndSize.Medium.dp)
              ) {
                uiState.headerDate?.let { date ->
                  stickyHeader {
                    Text(
                      text = stringResource(
                        Res.string.events_on,
                        date.toDateTime().date
                      ),
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.Black,
                    )
                  }
                }

                items(
                  items = uiState.eventDates,
                  key = { it.id }
                ) { eventDate ->
                  RadiusCard(
                    modifier = eventDate.url?.let { url ->
                      Modifier.clickable(
                        onClick = {
                          uriHandler.openUri(url)
                          onDismiss()
                        }
                      )
                    } ?: Modifier
                  ) {
                    Column(modifier = Modifier.padding(GlobalPaddingAndSize.Medium.dp)) {
                      with(eventDate) {
                        Text(
                          text = title,
                          style = MaterialTheme.typography.titleSmall,
                          fontWeight = FontWeight.Bold,
                          maxLines = 2,
                        )
                        date?.let {
                          Text(
                            text = date,
                            style = MaterialTheme.typography.bodyMedium,
                          )
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun EventDateScreenPreview() {
  EventDateScreen(
    uiState = Loading,
    onAction = {},
    onDismiss = {},
  )
}