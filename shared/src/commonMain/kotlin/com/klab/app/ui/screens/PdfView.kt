package com.klab.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.conamobile.pdfkmp.viewer.KmpPdfViewer
import com.klab.app.service.usecase.PlanningPdfService
import com.klab.app.ui.viewModel.meal.PdfPreviewViewModel
import org.koin.compose.koinInject

@Composable
fun PdfPreviewScreen(
    planId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pdfService: PlanningPdfService = koinInject()

    val viewModel: PdfPreviewViewModel = viewModel(
        factory = PdfPreviewViewModel.factory(pdfService)
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(planId) {
        viewModel.loadPdf(planId)
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()

                    Text("Gerando planejamento...")
                }
            }

            uiState.pdfBytes != null -> {
                KmpPdfViewer(
                    bytes = uiState.pdfBytes!!,
                    title = "Planejamento alimentar",
                    fileName = "klab_planejamento_$planId.pdf",
                    onBack = onBack,
                    showDownload = true,
                    showShare = true,
                    showSearch = true,
                    showPageIndicator = true
                )
            }

            uiState.error != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = uiState.error
                            ?: "Erro ao gerar PDF.",
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(
                        onClick = {
                            viewModel.loadPdf(planId)
                        }
                    ) {
                        Text("Tentar novamente")
                    }

                    TextButton(
                        onClick = onBack
                    ) {
                        Text("Voltar")
                    }
                }
            }
        }
    }
}