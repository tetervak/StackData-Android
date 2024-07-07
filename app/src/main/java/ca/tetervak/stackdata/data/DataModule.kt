package ca.tetervak.stackdata.data

import android.content.Context
import ca.tetervak.stackdata.data.local.ItemDao
import ca.tetervak.stackdata.data.local.ItemDatabase
import ca.tetervak.stackdata.data.repository.ItemRepository
import ca.tetervak.stackdata.data.repository.LocalItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideItemDatabase(
        @ApplicationContext applicationContext: Context
    ): ItemDatabase = ItemDatabase.getDatabase(applicationContext)

    @Singleton
    @Provides
    fun provideItemDao(
        itemDatabase: ItemDatabase
    ): ItemDao = itemDatabase.itemDao

    @Singleton
    @Provides
    fun provideItemRepository(
        itemDao: ItemDao
    ): ItemRepository = LocalItemRepository(itemDao)
}