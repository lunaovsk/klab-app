
package com.klab.app.infra.database.seed

import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.klab.app.infra.dao.FoodDao
import com.klab.app.infra.database.KlabDatabase
import klab.shared.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val CSV_PATH = "files/tbca_alimentos.csv"
private const val BATCH_SIZE = 500

@OptIn(ExperimentalResourceApi::class)
suspend fun tbcaSeed(
    database: KlabDatabase,
    foodDao: FoodDao
) = withContext(Dispatchers.IO) {
    if (foodDao.countFoods() > 0) {
        return@withContext
    }

    val csv = Res.readBytes(CSV_PATH).decodeToString()
    var importedCount = 0

    database.useWriterConnection { transactor ->
        transactor.immediateTransaction {
            if (foodDao.countFoods() > 0) { return@immediateTransaction }

            TbcaParserCsv.parse(
                csv = csv,
                batchSize = BATCH_SIZE
            ) { batch ->
                foodDao.insertAll(batch)
                importedCount += batch.size
            }
        }
    }

    check(importedCount > 0) { "Nenhum alimento foi importado do CSV." }
}
