package com.klab.app.domain.validation

class EntityExistsValidate<T>(
    private val entityName: String
) : EntityValidate<T> {

    override fun validateExists(entity: T?, id: Long): T {
        return entity ?: throw NoSuchElementException("$entityName not found $id")
    }

    override fun validateDeleted(affectedRows: Int, id: Long) {
        if (affectedRows == 0) {
            throw NoSuchElementException("$entityName not found $id")
        }
    }
}
