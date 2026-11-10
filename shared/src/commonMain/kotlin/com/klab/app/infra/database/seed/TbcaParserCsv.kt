
package com.klab.app.infra.database.seed

import com.klab.app.infra.local.FoodEntity
import com.klab.app.infra.local.MacrosEntity

internal object TbcaParserCsv {

    private val requiredColumns = listOf(
        "descricao",
        "grupo",
        "Energia_kcal",
        "Carboidrato_disponível_g",
        "Proteína_g",
        "Lipídios_g"
    )

    suspend fun parse(
        csv: String,
        batchSize: Int,
        onBatch: suspend (List<FoodEntity>) -> Unit
    ) {
        require(batchSize > 0) {
            "O lote nao pode estar vazio"
        }

        val batch = ArrayList<FoodEntity>(batchSize)
        val row = mutableListOf<String>()
        val field = StringBuilder()
        var header: List<String>? = null
        var indexes: Map<String, Int> = emptyMap()
        var index = 0
        var inQuotes = false

        fun finishField() {
            row.add(field.toString())
            field.clear()
        }

        suspend fun finishRow() {
            finishField()

            if (header == null) {
                val columns = row.toList()
                validateHeader(columns)
                header = columns
                indexes = columns.withIndex().associate { it.value to it.index }
            } else if (row.any { it.isNotBlank() }) {
                val columns = header

                require(row.size == columns?.size) {
                    "CSV invalido: esperado ${columns?.size} " +
                            "colunas encontradas ${row.size}"
                }

                fun value(column: String): String =
                    row[indexes.getValue(column)].trim()

                val description = value("descricao")

                if (description.isNotBlank()) {
                    batch.add(
                        FoodEntity(
                            description = description,
                            group = value("grupo"),
                            macrosEntity = MacrosEntity(
                                goalCalories = parseNumber(value("Energia_kcal")),
                                goalCarbohydrate = parseNumber(value("Carboidrato_disponível_g")),
                                goalProtein = parseNumber(value("Proteína_g")),
                                goalFat = parseNumber(value("Lipídios_g"))
                            )
                        )
                    )

                    if (batch.size >= batchSize) {
                        onBatch(batch.toList())
                        batch.clear()
                    }
                }
            }
            row.clear()
        }
        while (index < csv.length) {
            val char = csv[index]

            if (inQuotes) {
                when (char) {
                    '"' if index + 1 < csv.length &&
                            csv[index + 1] == '"' -> {
                        field.append('"')
                        index++
                    }

                    '"' -> inQuotes = false
                    else -> field.append(char)
                }
            } else {
                when (char) {
                    '"' -> { require(field.isEmpty()) {
                            "Aspas inesperadas na posicao $index"
                        }
                        inQuotes = true
                    }
                    ',' -> finishField()
                    '\r', '\n' -> {
                        if (char == '\r' &&
                            index + 1 < csv.length &&
                            csv[index + 1] == '\n'
                        ) { index++ }
                        finishRow()
                    }
                    else -> {
                        field.append(char)
                    }
                }
            }
            index++
        }

        require(!inQuotes) { "CSV invalido: campo com aspas nao foi fechado"}

        if (field.isNotEmpty() || row.isNotEmpty()) { finishRow()}

        require(header != null) { "CSV vazio ou sem cabecalho"}

        if (batch.isNotEmpty()) { onBatch(batch.toList())}
    }
    private fun validateHeader(header: List<String>) {
        val missing = requiredColumns.filterNot(header::contains)

        require(missing.isEmpty()) {
            "Colunas obrigatorias nao encontradas no CSV: " +
                    missing.joinToString()
        }
    }

    private fun parseNumber(rawValue: String): Float {
        if (rawValue.isBlank() ||
            rawValue == "-" ||
            rawValue.equals("tr", ignoreCase = true)
        ) { return 0f }

        val normalized = rawValue.replace(',', '.')
        val number = normalized.toFloatOrNull()

        require(number != null && number.isFinite() && number >= 0f) {
            "Valor nutricional invalido: '$rawValue'."
        }

        return number
    }
}
