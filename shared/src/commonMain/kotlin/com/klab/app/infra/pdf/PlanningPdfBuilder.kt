package com.klab.app.infra.pdf

import com.conamobile.pdfkmp.geometry.Padding
import com.conamobile.pdfkmp.layout.PageBreakStrategy
import com.conamobile.pdfkmp.pdf
import com.conamobile.pdfkmp.style.BorderStroke
import com.conamobile.pdfkmp.style.PdfColor
import com.conamobile.pdfkmp.unit.dp
import com.conamobile.pdfkmp.unit.sp
import com.klab.app.domain.dto.response.MealPlanResponse

class PlanningPdfBuilder {

    private val primary = PdfColor(0.15f, 0.44f, 0.29f)
    private val lightGreen = PdfColor(0.91f, 0.96f, 0.92f)
    private val darkText = PdfColor(0.18f, 0.23f, 0.20f)
    private val secondaryText = PdfColor(0.42f, 0.47f, 0.44f)
    private val borderColor = PdfColor(0.82f, 0.88f, 0.84f)

    fun build(plan: MealPlanResponse): ByteArray {
        val document = pdf {
            metadata {
                title = plan.title
                author = "KLab"
            }

            page {
                pageBreakStrategy = PageBreakStrategy.Slice

                column(spacing = 16.dp) {
                    column(spacing = 6.dp) {
                        text("KLab") {
                            fontSize = 28.sp
                            bold = true
                            color = primary
                        }

                        text("PLANEJAMENTO NUTRICIONAL") {
                            fontSize = 11.sp
                            color = secondaryText
                        }

                        divider(
                            thickness = 1.dp,
                            color = borderColor
                        )

                        text(plan.title) {
                            fontSize = 20.sp
                            bold = true
                            color = darkText
                        }
                    }

                    card(
                        background = lightGreen,
                        cornerRadius = 10.dp,
                        padding = Padding.all(16.dp)
                    ) {
                        column(spacing = 5.dp) {
                            text("Meta calórica diária") {
                                fontSize = 12.sp
                                color = secondaryText
                            }

                            text("${plan.goal.calorie.toInt()} kcal") {
                                fontSize = 24.sp
                                bold = true
                                color = primary
                            }
                        }
                    }

                    text("Refeições do planejamento") {
                        fontSize = 17.sp
                        bold = true
                        color = darkText
                    }

                    plan.meals
                        .sortedBy { it.displayOrder }
                        .forEach { meal ->
                            card(
                                background = PdfColor.White,
                                cornerRadius = 10.dp,
                                padding = Padding.all(16.dp),
                                border = BorderStroke(
                                    1.dp,
                                    borderColor
                                )
                            ) {
                                column(spacing = 12.dp) {
                                    card(
                                        background = lightGreen,
                                        cornerRadius = 7.dp,
                                        padding = Padding.all(12.dp)
                                    ) {
                                        column(spacing = 4.dp) {
                                            text(meal.name) {
                                                fontSize = 17.sp
                                                bold = true
                                                color = primary
                                            }

                                            text("Meta: ${meal.goal.calorie.toInt()} kcal") {
                                                fontSize = 11.sp
                                                color = secondaryText
                                            }
                                        }
                                    }

                                    if (meal.options.isEmpty()) {
                                        text("Nenhuma opção cadastrada.") {
                                            fontSize = 11.sp
                                            color = secondaryText
                                        }
                                    }

                                    meal.options
                                        .sortedBy { it.displayOrder }
                                        .forEachIndexed { index, option ->
                                            if (index > 0) {
                                                divider(
                                                    thickness = 0.5.dp,
                                                    color = borderColor
                                                )
                                            }

                                            column(spacing = 8.dp) {
                                                text(option.title) {
                                                    fontSize = 14.sp
                                                    bold = true
                                                    color = darkText
                                                }

                                                option.items.forEach { item ->
                                                    column(spacing = 3.dp) {
                                                        text(item.food?.description ?: "Alimento #${item.foodId}") {
                                                            fontSize = 11.sp
                                                            color = darkText
                                                        }

                                                        text("Quantidade: ${item.amountInGrams} g") {
                                                            fontSize = 10.sp
                                                            color = secondaryText
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                }
                            }
                        }

                    divider(
                        thickness = 0.5.dp,
                        color = borderColor
                    )

                    text("Documento gerado pelo KLab") {
                        fontSize = 10.sp
                        color = secondaryText
                    }
                }
            }
        }

        return document.toByteArray()
    }
}
