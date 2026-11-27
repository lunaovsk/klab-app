package com.klab.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klab.app.ui.components.MainButton
import com.klab.app.ui.components.SectionTitle
import com.klab.app.ui.components.SubText
import com.klab.app.ui.components.cards.HomeCard
import com.klab.app.ui.theme.*
import klab.shared.generated.resources.Res
import klab.shared.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    onStartPlanning: () -> Unit,
    onViewPlans: () -> Unit,
    onSearchFoods: () -> Unit,
    modifier: Modifier = Modifier
) {
    MaterialTheme {

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "Logo do KLab",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SectionTitle(
                        text = "KLab",
                        fontSize = 30.sp
                    )

                    SubText(
                        text = "Planejamento nutricional"
                    )
                }
            }

            HorizontalDivider(color = BorderLines)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = WhiteColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    SectionTitle(
                        text = "Novo planejamento",
                        fontSize = 20.sp
                    )

                    SubText(
                        text = "Organize um novo plano alimentar."
                    )

                    MainButton(
                        text = "+ Iniciar planejamento",
                        onClick = onStartPlanning,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            HomeCard(
                title = "Meus planejamentos",
                description = "Consulte e gerencie seus planos.",
                onClick = onViewPlans
            )

            HomeCard(
                title = "Consultar alimentos",
                description = "Explore os alimentos da tabela TBCA.",
                onClick = onSearchFoods
            )
        }
    }
}