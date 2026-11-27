package com.klab.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.klab.app.ui.screens.enums.PlanPeriodFilter
import com.klab.app.ui.theme.GreenDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanPeriodSelector(
    selectedFilter: PlanPeriodFilter,
    onFilterSelected: (PlanPeriodFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedFilter.label,
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Filtrar por")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenDetails,
                focusedLabelColor = GreenDetails
            ),
            modifier = Modifier
                .menuAnchor(
                    ExposedDropdownMenuAnchorType.PrimaryNotEditable
                )
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
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