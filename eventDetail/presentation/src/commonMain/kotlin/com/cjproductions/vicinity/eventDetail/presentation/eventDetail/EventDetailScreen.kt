@file:OptIn(FlowPreview::class)

package com.cjproductions.vicinity.eventDetail.presentation.eventDetail

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XLarge
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.AnimatedTopBar
import com.cjproductions.vicinity.core.presentation.ui.components.BOTTOM_SHEET_HEIGHT
import com.cjproductions.vicinity.core.presentation.ui.components.DefaultLabeledIcon
import com.cjproductions.vicinity.core.presentation.ui.components.HeaderContent
import com.cjproductions.vicinity.core.presentation.ui.components.ImageErrorContainer
import com.cjproductions.vicinity.core.presentation.ui.components.ImageLoadingContainer
import com.cjproductions.vicinity.core.presentation.ui.components.RoundedButton
import com.cjproductions.vicinity.core.presentation.ui.error.ErrorCard
import com.cjproductions.vicinity.core.presentation.ui.imageColorFilter
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.discover.domain.model.Status
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnChangeVenueClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnDateClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnRetryClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnShareClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnToggleLike
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnVenueClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailDestination.EventDates
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailViewState.Error
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailViewState.Loaded
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailViewState.Loading
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.components.EventCalendar
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.components.ModernLinkText
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.components.VenueCard
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.model.EventUi
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.model.VenueUi
import com.cjproductions.vicinity.likes.domain.LikeState
import com.cjproductions.vicinity.likes.domain.LikeState.Hide
import com.cjproductions.vicinity.likes.domain.LikeState.Show
import com.cjproductions.vicinity.location.presentation.MapUI
import com.cjproductions.vicinity.support.share.presentation.getShareIcon
import com.cjproductions.vicinity.support.tools.getCurrentTime
import com.cjproductions.vicinity.support.tools.toDateTime
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import compose.icons.FontAwesomeIcons
import compose.icons.LineAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.CalendarAlt
import compose.icons.fontawesomeicons.regular.Clock
import compose.icons.fontawesomeicons.solid.MapMarkerAlt
import compose.icons.lineawesomeicons.Heart
import compose.icons.lineawesomeicons.HeartSolid
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.eventdetail.presentation.generated.resources.Res.string
import vicinity.eventdetail.presentation.generated.resources.change_venue
import vicinity.eventdetail.presentation.generated.resources.event_dates
import vicinity.eventdetail.presentation.generated.resources.gallery
import vicinity.eventdetail.presentation.generated.resources.info
import vicinity.eventdetail.presentation.generated.resources.location
import vicinity.eventdetail.presentation.generated.resources.please_note
import kotlin.math.abs

@Composable
fun EventDetailScreenRoot(
  viewModel: EventDetailViewModel = koinViewModel(),
  goBack: () -> Unit,
  goToEventDates: (ids: List<String>, date: Double) -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val likeState by viewModel.likeState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.eventDetailDestination.collectLatest { event ->
      when (event) {
        is EventDates -> goToEventDates(event.ids, event.date)
      }
    }
  }
  EventDetailScreen(
    likeState = likeState,
    uiState = uiState,
    goBack = goBack,
    onViewAction = viewModel::onViewAction,
  )
}

@Composable
private fun EventDetailScreen(
  likeState: LikeState,
  uiState: EventDetailViewState,
  onViewAction: (EventDetailAction) -> Unit,
  goBack: () -> Unit,
) {
  var scrollPosition by rememberSaveable(
    stateSaver = ScrollPositionSaver
  ) { mutableStateOf(ScrollPosition(0, 0)) }
  val lazyListState = rememberLazyListState(
    initialFirstVisibleItemIndex = scrollPosition.index,
    initialFirstVisibleItemScrollOffset = scrollPosition.offset,
  )

  val scrollProgress by remember(lazyListState.firstVisibleItemIndex) {
    derivedStateOf {
      when {
        lazyListState.firstVisibleItemIndex > 0 -> 1f
        else -> 0f
      }
    }
  }
  val titleAlpha by remember { derivedStateOf { abs(scrollProgress - 1) } }
  val animatedTopBarBackgroundAlpha by animateFloatAsState(
    targetValue = scrollProgress,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioNoBouncy,
      stiffness = Spring.StiffnessHigh
    )
  )

  LaunchedEffect(lazyListState) {
    combine(
      snapshotFlow { lazyListState.firstVisibleItemIndex },
      snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
    ) { index, offset ->
      ScrollPosition(index = index, offset = offset)
    }.debounce(500L).collectLatest { position ->
      scrollPosition = position
    }
  }
  Box(
    contentAlignment = Alignment.TopCenter,
    modifier = Modifier.fillMaxSize().scrollable(
      state = lazyListState,
      orientation = Orientation.Vertical,
      reverseDirection = true
    )
  ) {
    when (uiState) {
      is Loading -> {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
      }

      is Loaded -> {
        val shouldShowEventDetail by remember {
          derivedStateOf { uiState.venues.size <= 1 || uiState.selectedVenueId != null }
        }
        LazyColumn(
          state = lazyListState,
          modifier = Modifier
            .fillMaxWidth(
              if (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
                == WindowWidthSizeClass.COMPACT
              ) 1f else 0.5f
            ).navigationBarsPadding(),
          contentPadding = PaddingValues(bottom = GlobalPaddingAndSize.Medium.dp)
        ) {
          item {
            val imageModifier = Modifier
              .fillMaxWidth()
              .height(BOTTOM_SHEET_HEIGHT).then(Modifier)
            SubcomposeAsyncImage(
              model = uiState.selectedEvent.mainImage,
              modifier = imageModifier,
              contentScale = ContentScale.FillBounds,
              contentDescription = null,
              colorFilter = imageColorFilter,
              loading = { ImageLoadingContainer(modifier = Modifier.fillMaxSize()) },
              error = { ImageErrorContainer(Modifier.fillMaxSize()) },
            )
          }

          item {
            Text(
              text = uiState.selectedEvent.title,
              color = MaterialTheme.colorScheme.onSurface.copy(alpha = titleAlpha),
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(
                top = GlobalPaddingAndSize.Medium.dp,
                start = GlobalPaddingAndSize.Medium.dp,
                end = GlobalPaddingAndSize.Medium.dp,
              ),
            )
          }
          if (shouldShowEventDetail) {
            with(uiState) {
              EventDetailContent(
                venues = venues,
                firstEvent = selectedEvent,
                eventItems = events,
                selectedVenueId = selectedVenueId,
                onAction = onViewAction,
                onResetScroll = { lazyListState.scrollToItem(0) }
              )
            }
          } else {
            VenueContent(
              venues = uiState.venues,
              onClick = {
                onViewAction(OnVenueClicked(it))
                lazyListState.scrollToItem(0)
              },
            )
          }
        }
      }

      is Error -> {
        ErrorCard(
          modifier = Modifier.align(Alignment.Center),
          onRetry = { onViewAction(OnRetryClicked) },
        )
      }
    }

    AnimatedTopBar(
      titleBackgroundAlpha = animatedTopBarBackgroundAlpha,
      title = (uiState as? Loaded)?.selectedEvent?.title,
      onBackClick = goBack,
      trailingContent = {
        Row(
          horizontalArrangement = Arrangement.spacedBy(XSmall.dp)
        ) {
          (likeState as? Show)?.let { likeState ->
            RoundedButton(
              icon = LineAwesomeIcons.HeartSolid.takeIf { likeState.liked }
                ?: LineAwesomeIcons.Heart,
              onClick = {
                onViewAction(
                  OnToggleLike(
                    isLiked = likeState.liked,
                    image = (uiState as? Loaded)?.selectedEvent?.mainImage,
                  )
                )
              },
            )
          }

          RoundedButton(
            icon = getShareIcon(),
            onClick = { onViewAction(OnShareClicked) },
          )
        }
      },
      modifier = Modifier.fillMaxWidth()
    )
  }
}

private fun LazyListScope.VenueContent(
  venues: List<VenueUi>,
  onClick: suspend (String) -> Unit,
) {
  items(count = venues.size) {
    val scope = rememberCoroutineScope()
    venues.getOrNull(it)?.let { venue ->
      VenueCard(
        venue = venue,
        onClick = { scope.launch { onClick(venue.id) } },
      )
    }
  }
}


private fun LazyListScope.EventDetailContent(
  venues: List<VenueUi>,
  firstEvent: EventUi,
  eventItems: List<EventScheduleUI>?,
  selectedVenueId: String?,
  onAction: (EventDetailAction) -> Unit,
  onResetScroll: suspend () -> Unit,
  headerModifier: Modifier = Modifier.padding(
    top = XLarge.dp,
    start = GlobalPaddingAndSize.Medium.dp,
    end = GlobalPaddingAndSize.Medium.dp,
    bottom = GlobalPaddingAndSize.Medium.dp,
  ),
) {
  item {
    venues.firstOrNull { it.id == selectedVenueId }?.let { venue ->
      DefaultLabeledIcon(
        leadingIcon = FontAwesomeIcons.Solid.MapMarkerAlt,
        text = venue.name,
        containerModifier = Modifier.padding(
          vertical = XSmall.dp,
          horizontal = GlobalPaddingAndSize.Medium.dp,
        )
      )
    }

    Column(
      verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
    ) {
      eventItems?.firstOrNull()?.dateTimes?.firstOrNull()?.toDateTime()?.let { date ->
        Row(
          horizontalArrangement = Arrangement.spacedBy(XSmall.dp),
          modifier = Modifier.padding(horizontal = GlobalPaddingAndSize.Medium.dp),
        ) {
          DefaultLabeledIcon(
            leadingIcon = FontAwesomeIcons.Regular.CalendarAlt,
            text = date.date,
            containerModifier = Modifier.weight(1f)
          )
          DefaultLabeledIcon(
            leadingIcon = FontAwesomeIcons.Regular.Clock,
            text = date.time,
            containerModifier = Modifier.weight(1f)
          )
        }
      }
    }
  }

  item {
    venues.firstOrNull { it.id == selectedVenueId }?.location?.let { location ->
      val scope = rememberCoroutineScope()
      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = headerModifier.fillMaxWidth(),
      ) {
        HeaderContent(text = stringResource(string.location))

        if (venues.size > 1) {
          Text(
            text = stringResource(string.change_venue),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Black,
            modifier = Modifier.clickable {
              onAction(OnChangeVenueClicked)
              scope.launch { onResetScroll() }
            }
          )
        }
      }

      MapUI(
        lat = location.latitude.toDouble(),
        long = location.longitude.toDouble(),
        modifier = Modifier
          .fillMaxWidth()
          .height(250.dp)
          .padding(
            bottom = XSmall.dp,
            start = XSmall.dp,
            end = XSmall.dp,
          )
          .clip(MaterialTheme.shapes.medium)

      )
    }
  }

  if (eventItems?.isNotEmpty() == true) {
    item {
      eventItems.let { eventItem ->
        HeaderContent(
          text = stringResource(
            string.event_dates,
            eventItem.map { it.dateTimes }.flatten().size
          ),
          modifier = headerModifier,
        )
        EventCalendar(
          eventItems = eventItem,
          onClickSelectedDate = { ids, date -> onAction(OnDateClicked(ids, date)) },
          modifier = Modifier.padding(horizontal = XSmall.dp),
        )
      }
    }
  }

  firstEvent.galleryImages?.let { galleryImages ->
    item {
      HeaderContent(
        text = stringResource(string.gallery),
        modifier = headerModifier,
      )
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
        contentPadding = PaddingValues(
          horizontal = GlobalPaddingAndSize.Medium.dp,
          vertical = GlobalPaddingAndSize.Medium.dp,
        )
      ) {
        items(galleryImages.size) { index ->
          Card(
            elevation = CardDefaults.elevatedCardElevation(0.dp),
            modifier = Modifier.size(200.dp)
          ) {
            AsyncImage(
              modifier = Modifier.fillMaxSize(),
              model = galleryImages[index],
              contentScale = ContentScale.FillBounds,
              contentDescription = null,
              colorFilter = ColorFilter.tint(
                color = Color.Black.copy(alpha = 0.6f),
                blendMode = BlendMode.Overlay
              )
            )
          }
        }
      }
    }
  }

  item {
    firstEvent.info?.let {
      HeaderContent(
        text = stringResource(string.info),
        modifier = headerModifier,
      )
      ModernLinkText(
        text = it,
        modifier = Modifier.padding(horizontal = GlobalPaddingAndSize.Medium.dp),
      )
    }

    firstEvent.pleaseNote?.let {
      HeaderContent(
        text = stringResource(string.please_note),
        modifier = headerModifier,
      )
      ModernLinkText(
        text = it,
        modifier = Modifier.padding(horizontal = GlobalPaddingAndSize.Medium.dp),
      )
    }
  }
}

@Preview
@Composable
private fun EventDetailScreenPreview() {
  VicinityTheme {
    val loadedUiState = Loaded(
      venues = listOf(
        VenueUi(
          id = "vocibus",
          name = "Theron Lloyd",
          city = "White Hawk",
          state = "Oregon",
          images = listOf(),
          country = "Belize",
          distance = "adhuc",
          location = null
        )
      ),
      selectedVenueId = "litora",
      events = listOf(
        EventScheduleUI(
          id = "liber",
          dateTimes = listOf(getCurrentTime().toLocalDateTime()!!),
        )
      ),
      selectedEvent = EventUi(
        id = "doctus",
        title = "explicari",
        locale = "ante",
        distance = "corrumpit",
        url = "https://search.yahoo.com/search?p=eros",
        info = "scripta",
        pleaseNote = "ullamcorper",
        mainImage = "movet",
        galleryImages = listOf(),
        dateTimeRange = null,
        status = Status.ON_SALE,
        classifications = listOf(),
        venues = listOf(),
        attractions = listOf()
      )

    )
    listOf(
      loadedUiState,
      Loading,
      Error(DataError.Network.SERVER_ERROR)
    ).forEach { state ->
      Column {
        EventDetailScreen(
          uiState = state,
          likeState = Hide,
          onViewAction = {},
          goBack = {},
        )
      }
    }
  }
}

data class ScrollPosition(
  val index: Int,
  val offset: Int,
)

val ScrollPositionSaver = Saver<ScrollPosition, List<Int>>(
  save = { listOf(it.index, it.offset) },
  restore = { ScrollPosition(it[0], it[1]) }
)