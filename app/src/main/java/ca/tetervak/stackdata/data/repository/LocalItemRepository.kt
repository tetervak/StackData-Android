package ca.tetervak.stackdata.data.repository

import ca.tetervak.stackdata.data.local.ItemDao
import ca.tetervak.stackdata.domain.StackItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalItemRepository(
    private val itemDao: ItemDao
): ItemRepository {

    override fun getStackItems(): Flow<List<StackItem>> =
        itemDao.getAllItemsFlow().map { list ->
            buildList(list.size) {
                for ((index, value) in list.withIndex()) {
                    add(StackItem(list.size - index, value))
                }
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun push(value: String) =
        withContext(Dispatchers.IO){
            itemDao.insert(value)
        }

    override suspend fun pop(): String =
        withContext(Dispatchers.IO) {
            itemDao.deleteLastItem()
        }
}