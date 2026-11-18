package com.klab.app.domain.validation

interface EntityValidate<T> {
    fun validateExists(entity: T?, id: Long): T
    fun validateDeleted(affectedRows: Int, id: Long)
}
