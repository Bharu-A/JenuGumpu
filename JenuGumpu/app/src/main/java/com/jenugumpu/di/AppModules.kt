package com.jenugumpu.di

import android.content.Context
import androidx.room.Room
import com.jenugumpu.data.local.dao.HoneyBatchDao
import com.jenugumpu.data.local.database.JenuGumpuDatabase
import com.jenugumpu.data.repository.HoneyRepositoryImpl
import com.jenugumpu.domain.repository.HoneyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): JenuGumpuDatabase =
        Room.databaseBuilder(context, JenuGumpuDatabase::class.java, "jenu_gumpu.db").build()

    @Provides
    fun provideDao(db: JenuGumpuDatabase): HoneyBatchDao = db.honeyBatchDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(impl: HoneyRepositoryImpl): HoneyRepository
}
