package com.cjproductions.vicinity.eventDetail.presentation.eventDetail.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXXSmall
import com.cjproductions.vicinity.core.presentation.ui.components.BOTTOM_SHEET_HEIGHT
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventScheduleUI
import com.cjproductions.vicinity.support.tools.toDouble
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.YearMonth
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.pluralStringResource
import vicinity.eventdetail.presentation.generated.resources.Res
import vicinity.eventdetail.presentation.generated.resources.event

@Composable
fun EventCalendar(
    eventItems: List<EventScheduleUI>,
    onClickSelectedDate: (ids: List<String>, date: Double) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedDateTimes = remember(eventItems) { eventItems.flatMap { it.dateTimes } }
    val eventsByDate = remember(selectedDateTimes) {
        selectedDateTimes.groupingBy { dateTime ->
            Triple(
                first = dateTime.year,
                second = dateTime.month,
                third = dateTime.dayOfMonth,
            )
        }.eachCount()
    }

    val eventsByMonth = remember(selectedDateTimes) {
        selectedDateTimes.groupingBy { dateTime -> dateTime.year to dateTime.month }.eachCount()
    }

    val startTimeRange = selectedDateTimes.first()
    val endTimeRange = selectedDateTimes.last()

    val isMoreThanOneMonth = remember(startTimeRange, endTimeRange) {
        val monthsDiff = (endTimeRange.year - startTimeRange.year) * MONTHS_SIZE +
            (endTimeRange.monthNumber - startTimeRange.monthNumber)
        monthsDiff > 0
    }

    val calendarState = rememberCalendarState(
        startMonth = YearMonth(
            year = startTimeRange.year,
            month = startTimeRange.month,
        ),
        endMonth = YearMonth(
            year = endTimeRange.year,
            month = endTimeRange.month,
        )
    )

    HorizontalCalendar(
        state = calendarState,
        contentHeightMode = ContentHeightMode.Fill,
        monthHeader = { month ->
            MonthHeader(
                month = month,
                eventCount = eventsByMonth[month.yearMonth.year to month.yearMonth.month] ?: 0,
            )
        },
        dayContent = { day ->
            if (day.position == DayPosition.MonthDate) {
                Day(
                    day = day,
                    eventCount = eventsByDate[
                        Triple(
                            first = day.date.year,
                            second = day.date.month,
                            third = day.date.dayOfMonth,
                        )
                    ] ?: 0,
                    onClick = { clickedDay ->
                        val eventIdsOnClickedDay = eventItems.filter { eventScheduleUI ->
                            eventScheduleUI.dateTimes.map { it.date }.contains(clickedDay.date)
                        }.map { it.id }
                        onClickSelectedDate(
                            eventIdsOnClickedDay,
                            clickedDay.date.toLocalDateTime().toDouble()
                        )
                    }
                )
            }
        },
        monthContainer = { calendarMonth, container ->
            Box(
                modifier = Modifier
                    .then(
                        if (isMoreThanOneMonth) Modifier
                            .width(BOTTOM_SHEET_HEIGHT) else Modifier.fillParentMaxWidth()
                    ).height(400.dp)
                    .padding(horizontal = XSmall.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium
                    ).padding(XSmall.dp)
            ) { container() }
        },
        modifier = modifier,
    )
}

@Composable
private fun Day(
    day: CalendarDay,
    eventCount: Int,
    onClick: (CalendarDay) -> Unit,
) { //fix day border later
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ).padding(XXXSmall.dp)
            .border(
                width = 1.dp,
                color = when {
                    eventCount > 0 -> MaterialTheme.colorScheme.primary
                    day.position == DayPosition.MonthDate -> Color.Transparent
                    else -> Color.Transparent
                },
                shape = MaterialTheme.shapes.medium
            )
            .padding(XXXSmall.dp)
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when {
                eventCount > 0 -> Color.Black
                day.position == DayPosition.MonthDate -> Color.Black
                else -> Color.Gray
            },
            fontSize = 14.sp,
            fontWeight = if (eventCount > 0) FontWeight.Bold else FontWeight.Normal,
        )

        if (eventCount > 1) {
            Text(
                text = "+ $eventCount",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun MonthHeader(
    month: CalendarMonth,
    eventCount: Int,
) {
    val daysOfWeek = remember(month) { month.weekDays.first().map { it.date.dayOfWeek } }

    Column {
        Text(
            text = "${month.yearMonth.month.name.getDisplayName()} ${month.yearMonth.year}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = XSmall.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = pluralStringResource(
                resource = Res.plurals.event,
                quantity = eventCount,
                eventCount,
            ),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = XSmall.dp),
            textAlign = TextAlign.Center
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.name.getDisplayName(),
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

private const val MONTHS_SIZE = 12

@Preview
@Composable
private fun RadiusCalendarPreview() {
    VicinityTheme {
        EventCalendar(
            eventItems = listOf(
                EventScheduleUI(
                    id = "platonem",
                    dateTimes = listOf(
                        LocalDateTime(
                            year = 2025,
                            monthNumber = 12,
                            dayOfMonth = 1,
                            hour = 11,
                            minute = 34,
                            second = 11,
                            nanosecond = 1000,
                        ),
                        LocalDateTime(
                            year = 2025,
                            monthNumber = 12,
                            dayOfMonth = 15,
                            hour = 14,
                            minute = 30,
                            second = 0,
                            nanosecond = 0,
                        ),
                        LocalDateTime(
                            year = 2025,
                            monthNumber = 12,
                            dayOfMonth = 15,
                            hour = 16,
                            minute = 0,
                            second = 0,
                            nanosecond = 0,
                        )
                    ),
                )
            ),
            onClickSelectedDate = { _, _ -> }
        )
    }
}

private fun String.getDisplayName() = substring(0, 3)