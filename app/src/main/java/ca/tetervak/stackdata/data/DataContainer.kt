package ca.tetervak.stackdata.data

import android.content.Context
import ca.tetervak.stackdata.data.local.ItemDatabase
import ca.tetervak.stackdata.data.repository.LocalItemRepository
import ca.tetervak.stackdata.data.repository.ItemRepository

class DataContainer(private val context: Context) {

    val itemRepository: ItemRepository by lazy {
        LocalItemRepository(ItemDatabase.getDatabase(context).itemDao)
    }
}