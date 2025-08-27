@file:OptIn(ExperimentalMaterial3Api::class)

package com.cjproductions.vicinity.search.presentation.datePicker

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXXSmall
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.support.tools.getCurrentTime
import com.cjproductions.vicinity.support.tools.toDateTime
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.YearMonth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.daysUntil
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.search.presentation.generated.resources.Res
import vicinity.search.presentation.generated.resources.date
import vicinity.search.presentation.generated.resources.select
import vicinity.search.presentation.generated.resources.select_date
import vicinity.search.presentation.generated.resources.to
import kotlin.math.roundToInt

@Composable
fun DatePickerRoot(
  viewModel: DatePickerViewModel = koinViewModel(),
  goBack: () -> Unit,
  goToFilter: (List<LocalDateTime>) -> Unit,
) {
  val dates by viewModel.dates.collectAsStateWithLifecycle()
  DatePickerScreen(
    goBack = goBack,
    goToFilter = goToFilter,
    dates = dates,
  )
}

@Composable
private fun DatePickerScreen(
  dates: List<LocalDateTime>,
  goBack: () -> Unit,
  goToFilter: (List<LocalDateTime>) -> Unit,
) {
  VicinityTheme {
    Scaffold(
      contentWindowInsets = WindowInsets.safeDrawing,
      topBar = {
        TopNavBarWithBack(
          title = stringResource(Res.string.select_date),
          onBackClick = goBack
        )
      }
    ) { padding ->
      var startOfSelectedDate by remember(dates) {
        mutableStateOf<LocalDateTime?>(
          dates.firstOrNull()
        )
      }
      var endOfSelectedDate by remember(dates) {
        mutableStateOf<LocalDateTime?>(
          if (dates.size > 1) dates.lastOrNull() else null
        )
      }
      Box(
        modifier = Modifier
          .padding(padding)
          .padding(
            start = Medium.dp,
            end = Medium.dp,
          )
      ) {
        getCurrentTime().toLocalDateTime()?.let { dateTime ->
          val calendarState = rememberCalendarState(
            startMonth = YearMonth(
              year = dateTime.year,
              month = dateTime.month,
            ),
            endMonth = YearMonth(
              year = dateTime.year.inc(),
              month = dateTime.month,
            ),
          )
          VerticalCalendar(
            state = calendarState,
            contentHeightMode = ContentHeightMode.Wrap,
            monthHeader = { month -> MonthHeader(month = month) },
            dayContent = { day ->
              if (day.position == DayPosition.MonthDate) {
                Day(
                  day = day,
                  currentDateTime = dateTime.date,
                  startOfSelectedDate = startOfSelectedDate?.date,
                  endOfSelectedDate = endOfSelectedDate?.date,
                  onClick = { clickedDay ->
                    when {
                      startOfSelectedDate != null && endOfSelectedDate != null -> {
                        endOfSelectedDate = null
                        startOfSelectedDate =
                          clickedDay.date.toLocalDateTime()
                      }

                      startOfSelectedDate == null -> {
                        startOfSelectedDate =
                          clickedDay.date.toLocalDateTime()
                      }

                      endOfSelectedDate == null -> {
                        if (clickedDay.date < startOfSelectedDate!!.date) {
                          startOfSelectedDate =
                            clickedDay.date.toLocalDateTime()
                        } else {
                          endOfSelectedDate =
                            clickedDay.date.toLocalDateTime(
                              shouldStartDay = false
                            )
                        }
                      }
                    }
                  }
                )
              }
            },
          )
        }

        startOfSelectedDate?.let { startOfSelectedDate ->
          var offset by remember { mutableStateOf(Offset.Zero) }
          val draggableState = rememberDraggable2DState { offset += it }
          Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = XXSmall.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .offset {
                IntOffset(
                  x = offset.x.roundToInt(),
                  y = offset.y.roundToInt(),
                )
              }
              .draggable2D(
                state = draggableState,
                enabled = true,
              )
              .padding(Medium.dp),
          ) {
            Column(
              modifier = Modifier.padding(Medium.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(XSmall.dp),
            ) {
              Text(
                text = pluralStringResource(
                  resource = Res.plurals.date,
                  quantity = listOfNotNull(
                    startOfSelectedDate,
                    endOfSelectedDate
                  ).size,
                  listOfNotNull(startOfSelectedDate, endOfSelectedDate).size
                ),
                style = MaterialTheme.typography.titleSmall,
              )

              endOfSelectedDate?.let { endOfSelectedDate ->
                Text(
                  text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                      append(startOfSelectedDate.toDateTime().date)
                    }
                    append("  ${stringResource(Res.string.to)}  ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                      append(endOfSelectedDate.toDateTime().date)
                    }
                  },
                  style = MaterialTheme.typography.titleMedium,
                )
              } ?: run {
                Text(
                  text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                      append(startOfSelectedDate.toDateTime().date)
                    }
                  },
                  style = MaterialTheme.typography.titleMedium
                )
              }

              RadiusButton(label = stringResource(Res.string.select)) {
                goToFilter(listOfNotNull(startOfSelectedDate, endOfSelectedDate))
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun MonthHeader(
  month: CalendarMonth,
) {
  Column {
    Text(
      text = "${month.yearMonth.month.name.shortDisplayName()} ${month.yearMonth.year}",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = XSmall.dp),
      textAlign = TextAlign.Center
    )

    val daysOfWeek = remember(month) { month.weekDays.first().map { it.date.dayOfWeek } }

    Row(modifier = Modifier.fillMaxWidth()) {
      daysOfWeek.forEach { dayOfWeek ->
        androidx.compose.material.Text(
          text = dayOfWeek.name.shortDisplayName(),
          modifier = Modifier.weight(1f),
          textAlign = TextAlign.Center,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = Color.Gray
        )
      }
    }

    Spacer(modifier = Modifier.height(XXSmall.dp))
  }
}

@Composable
private fun BoxScope.Day(
  day: CalendarDay,
  currentDateTime: LocalDate,
  startOfSelectedDate: LocalDate?,
  endOfSelectedDate: LocalDate?,
  onClick: (CalendarDay) -> Unit,
) {
  val isToday = (day.date.dayOfMonth == currentDateTime.dayOfMonth
      && day.date.monthNumber == currentDateTime.monthNumber
      && day.date.year == currentDateTime.year)

  val isStartOfSelectedDate = day.date.dayOfMonth == startOfSelectedDate?.dayOfMonth
      && day.date.monthNumber == startOfSelectedDate.monthNumber
      && day.date.year == startOfSelectedDate.year

  val isEndOfSelectedDate = day.date.dayOfMonth == endOfSelectedDate?.dayOfMonth
      && day.date.monthNumber == endOfSelectedDate.monthNumber
      && day.date.year == endOfSelectedDate.year

  val isMiddleOfSelectedDate = if (startOfSelectedDate != null && endOfSelectedDate != null) {
    val dateBetween = startOfSelectedDate.daysUntil(endOfSelectedDate)
    val currentDateBetween = day.date.daysUntil(endOfSelectedDate)
    currentDateBetween in 1 until dateBetween
  } else false


  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
      .align(Alignment.Center)
      .aspectRatio(1f)
      .then(
        if (isStartOfSelectedDate && endOfSelectedDate != null) {
          Modifier.background(
            color = Color.LightGray,
            shape = createSemiCircleShape(SemiCircleOrientation.LEFT)
          )
        } else if (isEndOfSelectedDate) {
          Modifier.background(
            color = Color.LightGray,
            shape = createSemiCircleShape(SemiCircleOrientation.RIGHT)
          )
        } else if (isMiddleOfSelectedDate) {
          Modifier.background(
            color = Color.LightGray,
            shape = RectangleShape
          )
        } else Modifier
      )
      .clip(CircleShape)
      .clickable(
        enabled = day.position == DayPosition.MonthDate,
        onClick = { onClick(day) }
      )
      .padding(XXXSmall.dp).then(
        if (isStartOfSelectedDate || isEndOfSelectedDate) Modifier.background(
          color = Color.Black,
          shape = CircleShape
        ) else Modifier
      )
      .then(
        if (isToday && !isMiddleOfSelectedDate) Modifier.border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.primary,
          shape = CircleShape
        ) else Modifier
      ).padding(XXXSmall.dp)
  ) {
    Text(
      text = day.date.dayOfMonth.toString(),
      color = when {
        isStartOfSelectedDate || isEndOfSelectedDate -> Color.White
        else -> Color.Gray
      },
      fontSize = 14.sp,
    )
  }
}

private fun String.shortDisplayName() = substring(0, 3)

@Preview
@Composable
fun DatePickerScreenPreview() {
  VicinityTheme {
    Column(
      modifier = Modifier.scrollable(
        state = rememberScrollState(),
        orientation = Orientation.Vertical,
      )
    ) {
      DatePickerScreen(
        dates = emptyList(),
        goBack = {},
        goToFilter = {}
      )
    }
  }
}

fun createSemiCircleShape(orientation: SemiCircleOrientation = SemiCircleOrientation.TOP): Shape {
  return GenericShape { size, _ ->
    val width = size.width
    val height = size.height
    val radius = minOf(width, height) / 2f

    when (orientation) {
      SemiCircleOrientation.TOP -> {
        moveTo(0f, height)
        lineTo(0f, radius)
        arcTo(
          rect = androidx.compose.ui.geometry.Rect(
            left = 0f,
            top = 0f,
            right = width,
            bottom = height
          ),
          startAngleDegrees = 180f,
          sweepAngleDegrees = 180f,
          forceMoveTo = false
        )
        lineTo(width, height)
        close()
      }

      SemiCircleOrientation.BOTTOM -> {
        // Semi-circle at the bottom
        moveTo(0f, 0f)
        lineTo(0f, radius)
        arcTo(
          rect = androidx.compose.ui.geometry.Rect(
            left = 0f,
            top = 0f,
            right = width,
            bottom = height
          ),
          startAngleDegrees = 180f,
          sweepAngleDegrees = -180f,
          forceMoveTo = false
        )
        lineTo(width, 0f)
        close()
      }

      SemiCircleOrientation.LEFT -> {
        // Semi-circle on the left
        moveTo(width, 0f)
        lineTo(radius, 0f)
        arcTo(
          rect = androidx.compose.ui.geometry.Rect(
            left = 0f,
            top = 0f,
            right = width,
            bottom = height
          ),
          startAngleDegrees = 270f,
          sweepAngleDegrees = 180f,
          forceMoveTo = false
        )
        lineTo(width, height)
        close()
      }

      SemiCircleOrientation.RIGHT -> {
        // Semi-circle on the right
        moveTo(0f, 0f)
        lineTo(radius, 0f)
        arcTo(
          rect = androidx.compose.ui.geometry.Rect(
            left = 0f,
            top = 0f,
            right = width,
            bottom = height
          ),
          startAngleDegrees = 270f,
          sweepAngleDegrees = -180f,
          forceMoveTo = false
        )
        lineTo(0f, height)
        close()
      }
    }
  }
}

enum class SemiCircleOrientation {
  TOP, BOTTOM, LEFT, RIGHT
}