package com.klab.app.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klab.app.domain.dto.response.MealPlanResponse
import com.klab.app.ui.theme.*

@Composable
fun MealPlanCard(
    plan: MealPlanResponse,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val formattedDate =
        "${plan.createdAt.day.toString().padStart(2, '0')}/" +
                "${plan.createdAt.month.ordinal.plus(1).toString().padStart(2, '0')}/" +
                "${plan.createdAt.year}"

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(WhiteColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = plan.title,
                        color = MainTexts,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Criado em $formattedDate",
                        color = SecondaryTexts,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            HorizontalDivider(color = BorderLines)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Abrir planejamento",
                    color = Button,
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = "→",
                    color = Button
                )
            }
        }
    }
}