package com.klab.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.klab.app.ui.theme.*

@Composable
fun FoodSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Digite o nome do alimento...",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Button,
            unfocusedBorderColor = BorderFields,
            focusedContainerColor = WhiteColor,
            unfocusedContainerColor = WhiteColor,
            focusedTextColor = MainTexts,
            unfocusedTextColor = MainTexts,
            focusedPlaceholderColor = SecondaryTexts,
            unfocusedPlaceholderColor = SecondaryTexts,
            cursorColor = Button
        )
    )
}