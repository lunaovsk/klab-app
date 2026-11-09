package com.klab.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klab.app.ui.components.*
import com.klab.app.ui.theme.*
import klab.shared.generated.resources.Res
import klab.shared.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen( onStartPlanning: () -> Unit) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(112.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "Logo do NutriPlan",
                    modifier = Modifier.size(112.dp).clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }

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
