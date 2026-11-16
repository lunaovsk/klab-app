package com.klab.app.ui.viewModel.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.klab.app.service.usecase.PlanningPdfService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PdfPreviewState(
    val isLoading: Boolean = false,
    val pdfBytes: ByteArray? = null,
    val error: String? = null
)

class PdfPreviewViewModel(
    private val planningPdfService: PlanningPdfService
) : ViewModel() {

    private val _uiState = MutableStateFlow(PdfPreviewState())
    val uiState = _uiState.asStateFlow()

    fun loadPdf(planId: Long) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {

            _uiState.value = PdfPreviewState(
                isLoading = true
            )

            try {
                val bytes = planningPdfService.generate(planId)

                _uiState.value = PdfPreviewState(
                    pdfBytes = bytes
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = PdfPreviewState(
                    error = e.message
                        ?: "Não foi possível gerar o PDF."
                )
            }
        }
    }

    companion object {
        fun factory(
            planningPdfService: PlanningPdfService
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PdfPreviewViewModel(planningPdfService)
            }
        }
    }
}