package com.klab.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klab.app.ui.theme.*

@Composable
fun FoodGroupSelector(
    groups: List<String>,
    selectedGroup: String?,
    onGroupSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {

        TextButton(
            onClick = { expanded = true }
        ) {
            Text(
                text = "Filtrar por",
                color = GreenDetails
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Todos os grupos") },
                onClick = {
                    onGroupSelected(null)
                    expanded = false
                }
            )

            groups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group) },
                    onClick = {
                        onGroupSelected(group)
                        expanded = false
                    }
                )
            }
        }
    }
}
