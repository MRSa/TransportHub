package net.osdn.ja.gokigen.transporthub.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DataContent::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ReceivedDataHolder: RoomDatabase()
{
    abstract fun storageDao(): DataContentDao
}
