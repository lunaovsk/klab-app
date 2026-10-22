
package com.klab.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klab.app.ui.theme.*

@Composable
fun MacronutrientCard(
    name: String,
    percentage: String,
    onPercentageChange: (String) -> Unit,
    grams: String,
    nutrientColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = nutrientColor.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                        .padding(top = 12.dp)
                ) {
                    Text(
                        text = name,
                        color = MainTexts,
                        fontSize = 15.sp
                    )
                }

                PercentageInput(
                    value = percentage,
                    onValueChange = onPercentageChange
                )
            }

            val percentageValue =
                percentage.toIntOrNull() ?: 0

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(nutrientColor.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(
                            (percentageValue / 100f)
                                .coerceIn(0f, 1f)
                        )
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(nutrientColor)
                )
            }

            Text(
                text = "Meta calculada: $grams g/dia",
                color = SecondaryTexts,
                fontSize = 13.sp
            )
        }
    }
}
