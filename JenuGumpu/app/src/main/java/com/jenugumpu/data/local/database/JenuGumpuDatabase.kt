package com.jenugumpu.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jenugumpu.data.local.dao.HoneyBatchDao
import com.jenugumpu.data.local.entity.HoneyBatchEntity

@Database(entities = [HoneyBatchEntity::class], version = 1, exportSchema = false)
abstract class JenuGumpuDatabase : RoomDatabase() {
    abstract fun honeyBatchDao(): HoneyBatchDao
}
