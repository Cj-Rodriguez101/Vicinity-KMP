package com.cjproductions.vicinity.support.tools

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit.MILLISECONDS

fun String.toLocalDateTime(): LocalDateTime? {
  return try {
    val formatter = LocalDateTime.Format {
      date(LocalDate.Formats.ISO)
      char('T')
      time(LocalTime.Formats.ISO)
      char('Z')
    }

    LocalDateTime.parse(
      input = this,
      format = formatter,
    )
  } catch (e: Exception) {
    println(TAG + " toLocalDateTime" + e.message)
    null
  }
}

fun Instant.toLocalDateTime(timeZone: TimeZone): LocalDateTime? {
  return try {
    this.toLocalDateTime(timeZone)
  } catch (e: Exception) {
    println(TAG + " " + e.message)
    null
  }
}

fun Double.toLocalDateTime(): LocalDateTime? {
  return try {
    Instant.fromEpochMilliseconds(this.toLong())
      .toLocalDateTime(TimeZone.currentSystemDefault())
  } catch (e: Exception) {
    println(TAG + " " + e.message)
    null
  }
}

fun getCurrentTime(): Double {
  return Clock.System.now().toEpochMilliseconds().milliseconds.toDouble(MILLISECONDS)
}

fun LocalDateTime.toDouble(): Double {
  return toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds().toDouble()
}


fun LocalDateTime.toDateTime(): DateTime {
  val dateFormat = LocalDateTime.Format {
    dayOfMonth(padding = Padding.ZERO)
    char('-')
    monthNumber(padding = Padding.ZERO)
    char('-')
    year()
  }

  val hour12 = when {
    this.hour == 0 -> 12
    this.hour > 12 -> this.hour - 12
    else -> this.hour
  }
  val amPm = if (this.hour < 12) "am" else "pm"
  val minute = this.minute.toString().padStart(2, '0')

  return DateTime(
    date = this.format(dateFormat),
    time = "${hour12}:${minute}${amPm}"
  )
}

fun LocalDate.toLocalDateTime(shouldStartDay: Boolean = true): LocalDateTime {
  val startInstant = atStartOfDayIn(TimeZone.currentSystemDefault())
  return (if (shouldStartDay) startInstant else startInstant.plus(23, DateTimeUnit.HOUR))
    .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDateTime.getDateTimeString() = this.format(LocalDateTime.Formats.ISO)

data class DateTime(
  val date: String,
  val time: String,
)

private const val TAG = "DateTimeExt"