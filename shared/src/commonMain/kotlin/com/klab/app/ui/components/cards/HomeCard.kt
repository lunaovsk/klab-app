package com.klab.app.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klab.app.ui.theme.*

@Composable
fun HomeCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary) {
                GreenDetails
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    color = if (isPrimary) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MainTexts
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = description,
                    color = if (isPrimary) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        SecondaryTexts
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = "→",
                color = if (isPrimary) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    GreenDetails
                },
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}