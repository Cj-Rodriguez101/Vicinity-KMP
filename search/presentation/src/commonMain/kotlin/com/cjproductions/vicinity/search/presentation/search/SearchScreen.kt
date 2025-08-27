package com.cjproductions.vicinity.search.presentation.search

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.DefaultLabeledIcon
import com.cjproductions.vicinity.core.presentation.ui.components.LabeledIcon
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusTextField
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.search.domain.model.RecentSearch
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnClearEventSearch
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnClearLocationSearch
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnEventSearchChanged
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnLocationSearchChanged
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnSearchClicked
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnSearchItemClicked
import com.cjproductions.vicinity.search.presentation.search.SearchDestination.Back
import com.cjproductions.vicinity.search.presentation.search.SearchDestination.SearchResult
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.History
import compose.icons.fontawesomeicons.solid.Search
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.core.presentation.ui.generated.resources.Close
import vicinity.core.presentation.ui.generated.resources.Marker
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.search.presentation.generated.resources.choose_location
import vicinity.search.presentation.generated.resources.`in`
import vicinity.search.presentation.generated.resources.recent
import vicinity.search.presentation.generated.resources.search
import vicinity.search.presentation.generated.resources.search_label
import vicinity.search.presentation.generated.resources.Res as SearchRes

@Composable
fun SearchScreenRoot(
  viewModel: SearchViewModel = koinViewModel(),
  goBack: () -> Unit,
  onSearchResult: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  viewModel.destination.observeAsEvents {
    when (it) {
      is SearchResult -> onSearchResult()
      Back -> goBack()
    }
  }
  SearchScreen(
    uiState = uiState,
    onAction = viewModel::onAction,
  )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
  uiState: SearchViewState,
  onAction: (SearchAction) -> Unit,
) {
  BackHandler(
    enabled = true,
    onBack = { onAction(SearchAction.OnBackClick) },
  )
  VicinityTheme {
    Scaffold(
      contentWindowInsets = WindowInsets.safeDrawing,
      topBar = {
        TopNavBarWithBack(
          title = stringResource(SearchRes.string.search),
          onBackClick = { onAction(SearchAction.OnBackClick) },
        )
      }
    ) { padding ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(padding)
      ) {
        Column(modifier = Modifier.padding(horizontal = Medium.dp)) {
          RadiusTextField(
            text = uiState.eventSearch.field,
            state = uiState.eventSearch.state,
            hint = stringResource(SearchRes.string.search_label),
            leadingContent = {
              Icon(
                imageVector = FontAwesomeIcons.Solid.Search,
                contentDescription = null,
                modifier = Modifier.size(Medium.dp),
              )
            },
            trailingContent = {
              if (uiState.eventSearch.field.isNotEmpty()) {
                Icon(
                  imageVector = vectorResource(Res.drawable.Close),
                  contentDescription = null,
                  modifier = Modifier
                    .size(Medium.dp)
                    .clickable { onAction(OnClearEventSearch) },
                )
              }
            },
            onTextChange = { onAction(OnEventSearchChanged(it)) },
          )

          Spacer(modifier = Modifier.height(XSmall.dp))

          RadiusTextField(
            text = uiState.locationSearch.field,
            state = uiState.locationSearch.state,
            hint = stringResource(SearchRes.string.choose_location),
            leadingContent = {
              Icon(
                imageVector = vectorResource(Res.drawable.Marker),
                contentDescription = null,
                modifier = Modifier.size(Medium.dp)
              )
            },
            trailingContent = {
              if (uiState.locationSearch.field.isNotEmpty()) {
                Icon(
                  imageVector = vectorResource(Res.drawable.Close),
                  contentDescription = null,
                  modifier = Modifier.size(Medium.dp)
                    .clickable {
                      onAction(OnClearLocationSearch)
                    }
                )
              }
            },
            onTextChange = { onAction(OnLocationSearchChanged(it)) },
          )
        }
        LazyColumn(
          modifier = Modifier.weight(1f),
          contentPadding = PaddingValues(horizontal = Medium.dp)
        ) {
          items(items = uiState.currentSearches) { currentSearch ->
            DefaultLabeledIcon(
              leadingIcon = FontAwesomeIcons.Solid.Search,
              text = currentSearch.keyword ?: currentSearch.location.orEmpty(),
              containerModifier = Modifier.fillMaxWidth().clickable {
                onAction(OnSearchItemClicked(currentSearch.toRecentSearch()))
              }.padding(vertical = Medium.dp),
            )
          }

          if (uiState.recentSearches.isNotEmpty()) {
            item {
              Text(
                text = stringResource(SearchRes.string.recent),
                modifier = Modifier.padding(top = Medium.dp)
              )
            }
          }

          items(items = uiState.recentSearches) { recentSearch ->
            LabeledIcon(
              leadingIcon = FontAwesomeIcons.Solid.History,
              textContent = { modifier ->
                Text(
                  text = getSearchKeywordLocation(recentSearch),
                  modifier = modifier
                )
              },
              trailingIcon = {
                Icon(
                  imageVector = vectorResource(Res.drawable.Close),
                  contentDescription = null,
                  modifier = Modifier.size(Medium.dp)
                    .clickable {
                      onAction(SearchAction.OnDeleteSearchItem(recentSearch))
                    }
                )
              },

              modifier = Modifier.fillMaxWidth().clickable {
                onAction(OnSearchItemClicked(recentSearch))
              }.padding(vertical = Medium.dp),
            )
          }
        }

        with(uiState) {
          RadiusButton(
            label = stringResource(SearchRes.string.search),
            enabled = isSearchButtonEnabled,
            loading = isSearchButtonLoading,
            onClick = { onAction(OnSearchClicked) },
            modifier = Modifier
              .fillMaxWidth()
              .padding(
                horizontal = Medium.dp,
                vertical = XSmall.dp,
              )
          )
        }
      }
    }
  }
}

@Composable
private fun getSearchKeywordLocation(recentSearch: RecentSearch): AnnotatedString =
  buildAnnotatedString {
    if (recentSearch.name?.isNotEmpty() == true) {
      withStyle(
        SpanStyle(
          fontWeight = FontWeight.Bold
        )
      ) { append(recentSearch.name) }

      recentSearch.location?.let { location ->
        append(" ${stringResource(SearchRes.string.`in`)} ")
        withStyle(
          SpanStyle(
            fontWeight = FontWeight.Bold
          )
        ) {
          append(location)
        }
      }
    } else {
      withStyle(
        SpanStyle(fontWeight = FontWeight.Bold)
      ) { append(recentSearch.location) }
    }
  }

@Preview
@Composable
fun SearchScreenPreview() {
  VicinityTheme {
    SearchScreen(
      uiState = SearchViewState(),
      onAction = {},
    )
  }
}