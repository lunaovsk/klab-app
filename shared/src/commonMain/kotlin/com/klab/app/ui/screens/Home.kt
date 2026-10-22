package com.klab.app.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klab.app.ui.components.*
import com.klab.app.ui.theme.*

@Composable
@Preview
fun HomeScreen( onStartPlanning: () -> Unit ) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .background(GreenDetails, CircleShape),
                contentAlignment = Alignment.Center
            ) {}

            Spacer(modifier = Modifier.height(28.dp))

            SectionTitle(
                text = "Klab",
                fontSize = 36.sp,
            )

            Spacer(modifier = Modifier.height(48.dp))

            MainButton(
                text = "Iniciar planejamento",
                onClick = onStartPlanning,
                modifier = Modifier.width(260.dp)
            )
        }
    }
}
