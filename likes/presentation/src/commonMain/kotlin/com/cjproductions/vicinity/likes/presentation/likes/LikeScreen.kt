@file:OptIn(ExperimentalMaterialApi::class)

package com.cjproductions.vicinity.likes.presentation.likes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.HeaderContent
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusVerticalGrid
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.error.ErrorCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.core.presentation.ui.generated.resources.likes
import vicinity.core.presentation.ui.generated.resources.no_events
import vicinity.likes.presentation.generated.resources.Res
import vicinity.likes.presentation.generated.resources.no_events
import vicinity.likes.presentation.generated.resources.sign_in
import vicinity.likes.presentation.generated.resources.sign_in_to_view_liked_events
import vicinity.core.presentation.ui.generated.resources.Res as CoreRes

@Composable
fun LikeScreenRoot(
  viewModel: LikesViewModel = koinViewModel(),
  onBackClick: () -> Unit,
  onSignInClick: () -> Unit,
  onItemClick: (String) -> Unit,
) {
  val likes = viewModel.likes.collectAsLazyPagingItems()
  val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
  LikeScreen(
    likes = likes,
    isLoggedIn = isLoggedIn,
    onBackClick = onBackClick,
    onItemClick = { onItemClick(it) },
    onSignInClick = onSignInClick,
    onAction = viewModel::onAction
  )
}

@Composable
fun LikeScreen(
  likes: LazyPagingItems<LikeUiModel>,
  isLoggedIn: Boolean,
  onBackClick: () -> Unit,
  onSignInClick: () -> Unit,
  onAction: (LikeAction) -> Unit,
  onItemClick: (String) -> Unit,
) {
  Scaffold(
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = {
      TopNavBarWithBack(
        title = stringResource(CoreRes.string.likes),
        onBackClick = onBackClick
      )
    }
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      contentAlignment = Alignment.Center
    ) {
      when {
        likes.loadState.refresh is LoadState.Loading && likes.itemCount == 0 -> {
          CircularProgressIndicator()
        }

        likes.loadState.refresh is LoadState.Error && likes.itemCount == 0 -> {
          ErrorCard(
            onRetry = { likes.retry() },
          )
        }

        isLoggedIn.not() -> {
          Column(
            verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Image(
              imageVector = vectorResource(Res.drawable.no_events),
              contentDescription = stringResource(CoreRes.string.no_events),
              modifier = Modifier.size(100.dp)
            )
            Text(
              text = stringResource(Res.string.sign_in_to_view_liked_events),
              style = MaterialTheme.typography.titleMedium,
            )

            RadiusButton(
              label = stringResource(Res.string.sign_in),
              onClick = onSignInClick,
              modifier = Modifier.padding(horizontal = GlobalPaddingAndSize.Medium.dp)
            )
          }
        }

        likes.loadState.refresh is LoadState.NotLoading &&
            likes.itemCount == 0 &&
            likes.loadState.source.refresh is LoadState.NotLoading -> {
          Text(text = stringResource(CoreRes.string.no_events))
        }

        else -> {
          Column(modifier = Modifier.padding(horizontal = GlobalPaddingAndSize.Medium.dp)) {
            RadiusVerticalGrid(
              horizontalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
              modifier = Modifier.weight(1f)
            ) {
              items(
                count = likes.itemCount,
                span = {
                  if (likes[it] is LikeUiModel.Header) GridItemSpan(
                    maxLineSpan
                  ) else GridItemSpan(1)
                }
              ) {
                val likeModel = likes[it]
                when (likeModel) {
                  is LikeUiModel.Header -> {
                    HeaderContent(
                      text = likeModel.title.asString(),
                      modifier = Modifier.padding(vertical = GlobalPaddingAndSize.Medium.dp)
                    )
                  }

                  is LikeUiModel.Item -> {
                    with(likeModel) {
                      LikeCard(
                        like = like,
                        onLikeClick = { onAction(LikeAction.OnLikeClick(like)) },
                        onItemClick = { onItemClick(like.title) },
                        onShareClick = {
                          onAction(
                            LikeAction.OnShareClick(
                              like.title
                            )
                          )
                        },
                        modifier = Modifier
                          .padding(vertical = XSmall.dp)
                          .animateItem()
                      )
                    }
                  }

                  else -> Unit
                }
              }

              when (likes.loadState.append) {
                is LoadState.Loading -> {
                  item {
                    Box(
                      modifier = Modifier.height(100.dp),
                      contentAlignment = Alignment.Center,
                    ) {
                      CircularProgressIndicator()
                    }
                  }
                }

                is LoadState.Error -> {
                  item { ErrorCard(onRetry = likes::retry) }
                }

                else -> Unit
              }
            }
          }
        }
      }
    }
  }
}