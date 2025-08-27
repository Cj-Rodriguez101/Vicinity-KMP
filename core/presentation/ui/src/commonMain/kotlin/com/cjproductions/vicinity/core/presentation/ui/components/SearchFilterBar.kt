package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterBar(
    filterCount: Int,
    textContent: @Composable () -> Unit,
    onClick: () -> Unit,
    onFilterClick: () -> Unit,
) {
    SearchBar(
        textContent = textContent,
        trailingContent = {
            BadgedBox(
                badge = {
                    if (filterCount > 0) {
                        Badge(
                            contentColor = MaterialTheme.colorScheme.surface,
                            containerColor = MaterialTheme.colorScheme.primary,
                        ) {
                            Text(text = filterCount.toString())
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Filter,
                    contentDescription = null,
                    modifier = Modifier.size(GlobalPaddingAndSize.Medium.dp)
                        .clickable { onFilterClick() },
                )
            }
        },
        onClick = onClick,
    )
}