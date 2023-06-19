package net.osdn.ja.gokigen.transporthub.mobile.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.osdn.ja.gokigen.transporthub.mobile.DbSingleton
import net.osdn.ja.gokigen.transporthub.mobile.storage.DataContent

@Parcelize
data class DetailData(var title: String, var value: String, var hash: String) : Parcelable

class DetailModel(val id: Int)
{
    private var isRefreshing = false
    lateinit var detailData: DetailData
    var dataContent: DataContent? = null
    init
    {
        update()
    }

    fun dataInitialized() : Boolean
    {
        return (::detailData.isInitialized)
    }

    private fun update()
    {
        try
        {
            val thread = Thread {
                if (!isRefreshing)
                {
                    isRefreshing = true
                    val storageDao = DbSingleton.db.storageDao()
                    dataContent = storageDao.findById(id)
                    detailData = DetailData(title = dataContent?.title ?: "?", value = dataContent?.note ?: "???", hash = dataContent?.hashValue ?: ">>")
                    //Log.v("DataDetail", "DataDetail($id)\n${detailData.title}\n${detailData.value}")
                    isRefreshing = false
                }
            }
            thread.start()
            try
            {
                thread.join()
            }
            catch (ee: Exception)
            {
                ee.printStackTrace()
            }
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
