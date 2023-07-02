package net.osdn.ja.gokigen.transporthub.storage

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DataContent::class], version = 2,   autoMigrations = [ AutoMigration (from = 1, to = 2) ], exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class ReceivedDataHolder: RoomDatabase()
{
    abstract fun storageDao(): DataContentDao
}
