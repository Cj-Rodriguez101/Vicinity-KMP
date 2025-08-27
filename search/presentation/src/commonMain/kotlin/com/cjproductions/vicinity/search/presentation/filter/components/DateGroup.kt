package com.cjproductions.vicinity.search.presentation.filter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.support.search.DateType
import com.cjproductions.vicinity.support.search.DateType.AnyDate
import com.cjproductions.vicinity.support.search.DateType.Custom
import com.cjproductions.vicinity.support.search.DateType.ThisWeekend
import com.cjproductions.vicinity.support.search.DateType.Today
import com.cjproductions.vicinity.support.search.DateType.Tomorrow
import com.cjproductions.vicinity.support.tools.toDateTime
import kotlinx.datetime.LocalDateTime
import vicinity.search.presentation.generated.resources.Res
import vicinity.search.presentation.generated.resources.any_date
import vicinity.search.presentation.generated.resources.choose_a_date
import vicinity.search.presentation.generated.resources.this_weekend
import vicinity.search.presentation.generated.resources.today
import vicinity.search.presentation.generated.resources.tomorrow

@Composable
fun DateGroup(
  selectedDateType: DateType,
  customDates: List<LocalDateTime>?,
  onDateTypeSelected: (DateType, List<LocalDateTime>?) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(verticalArrangement = Arrangement.spacedBy(XSmall.dp)) {
    DateType.entries.forEach { dateType ->
      val dateGroup = DateGroup(
        dateType = dateType,
        title = dateType.toString(customDates).asString(),
      )
      with(dateGroup) {
        SingleSelectItem(
          title = title,
          selected = selectedDateType == dateType,
          onClick = { onDateTypeSelected(dateType, customDates) },
          modifier = modifier,
        )
      }
    }
  }
}

data class DateGroup(
  val dateType: DateType,
  val title: String,
)

private fun DateType.toString(dates: List<LocalDateTime>?): UIText {
  return when (this) {
    AnyDate -> UIText.StringResourceId(Res.string.any_date)
    Today -> UIText.StringResourceId(Res.string.today)
    Tomorrow -> UIText.StringResourceId(Res.string.tomorrow)
    ThisWeekend -> UIText.StringResourceId(Res.string.this_weekend)
    Custom -> dates?.let { date ->
      when (date.size) {
        1 -> UIText.DynamicString(date.first().toDateTime().date)
        2 -> {
          UIText.DynamicString(
            "${date.first().toDateTime().date} - ${date.last().toDateTime().date}"
          )
        }

        else -> UIText.StringResourceId(Res.string.choose_a_date)
      }
    } ?: run { UIText.StringResourceId(Res.string.choose_a_date) }
  }
}