package com.klab.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.klab.app.ui.theme.*

@Composable
@Preview
fun NumericInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        suffix = { Text(unit) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MainTexts,
            unfocusedBorderColor = MainTexts.copy(alpha = 0.35f),
            focusedTextColor = MainTexts,
            unfocusedTextColor = MainTexts
        )
    )
}

@Composable
@Preview
fun PercentageInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (
                newValue.length <= 5 &&
                newValue.all { it.isDigit() }
            ) {
                val number = newValue.toIntOrNull()

                if (newValue.isEmpty() ||
                    (number != null && number <= 100)
                ) {
                    onValueChange(newValue)
                }
            }
        },
        modifier = modifier.width(104.dp),
        suffix = { Text("%") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        colors = androidx.compose.material3
            .OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MainTexts,
                unfocusedBorderColor = MainTexts.copy(alpha = 0.35f)
            )
    )
}
