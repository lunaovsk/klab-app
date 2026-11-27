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
                color = Button
            )
        }

        DropdownMenu(
            expanded = expanded,
            containerColor = WhiteColor,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text="Todos os grupos",color = MainTexts) },
                onClick = {
                    onGroupSelected(null)
                    expanded = false
                }
            )

            groups.forEach { group ->
                DropdownMenuItem(
                    text = { Text( text=group,color = MainTexts,) },
                    onClick = {
                        onGroupSelected(group)
                        expanded = false
                    }
                )
            }
        }
    }
}
