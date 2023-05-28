package net.osdn.ja.gokigen.transporthub.mobile

import android.app.Application
import androidx.room.Room
import net.osdn.ja.gokigen.transporthub.mobile.storage.ReceivedDataHolder

class DbSingleton : Application()
{
    companion object
    {
        lateinit var db: ReceivedDataHolder
    }

    override fun onCreate()
    {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            ReceivedDataHolder::class.java, "received-data-database"
        ).build()
    }
}
