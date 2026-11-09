package com.klab.app.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klab.app.ui.theme.*

@Composable
fun PlanningCard(
    title: String,
    optionsCount: Int,
    calories: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val optionsText = if (optionsCount == 0) {
            "Nenhuma opção cadastrada"
        } else { "$optionsCount opção(s) cadastrada(s)" }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = MainTexts,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = optionsText,
                color = SecondaryTexts,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "$calories kcal por opção",
                color = SecondaryTexts,
                style = MaterialTheme.typography.bodySmall
            )
        }
        TextButton(onClick = onClick) {
            Text(text = "Detalhar", color = GreenDetails)
        }
    }
}