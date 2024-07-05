package ca.tetervak.stackdata.data.repository

import ca.tetervak.stackdata.domain.StackItem
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    // the first item of the list is the top of the stack
    fun getStackItems(): Flow<List<StackItem>>

    suspend fun push(value: String)

    suspend fun pop(): String
}