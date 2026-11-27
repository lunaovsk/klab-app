package com.klab.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.klab.app.ui.screens.enums.PlanPeriodFilter
import com.klab.app.ui.theme.*

@Composable
fun PlanPeriodSelector(
    selectedFilter: PlanPeriodFilter,
    onFilterSelected: (PlanPeriodFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {

        TextButton(
            onClick = { expanded = true }
        ) {
            Text(
                text = "Filtrar por",
                color = Button
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            PlanPeriodFilter.entries.forEach { filter ->

                DropdownMenuItem(
                    text = {
                        Text(filter.label)
                    },
                    onClick = {
                        onFilterSelected(filter)
                        expanded = false
                    }
                )
            }
        }
    }
}