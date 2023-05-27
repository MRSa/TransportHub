package net.osdn.ja.gokigen.transporthub.presentation.model

import android.util.Log
import net.osdn.ja.gokigen.transporthub.DbSingleton

class DetailModel(val id: Int)
{
    private var isRefreshing = false
    var value:String = ""
    var title:String = "....."
    init
    {
        update()
    }

    fun update()
    {
        try
        {
            val thread = Thread {
                if (!isRefreshing)
                {
                    isRefreshing = true
                    val storageDao = DbSingleton.db.storageDao()
                    val data = storageDao.findById(id)
                    value = data?.note ?: "???"
                    title = data?.title ?: "?"
                    Log.v("DataDetail", "DataDetail($id)\n$title\n$value")
                    isRefreshing = false
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun deleteContent()
    {
        try
        {
            val thread = Thread {
                val storageDao = DbSingleton.db.storageDao()
                val content = storageDao.findById(id)
                if (content != null)
                {
                    storageDao.delete(content)
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}
