package net.osdn.ja.gokigen.transporthub.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.osdn.ja.gokigen.transporthub.DbSingleton

@Parcelize
data class DetailData(var title: String, var value: String, var hash: String) : Parcelable

class DetailModel(val id: Int)
{
    private var isRefreshing = false
    lateinit var detailData: DetailData
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
                    val data = storageDao.findById(id)
                    detailData = DetailData(title = data?.title ?: "?", value = data?.note ?: "???", hash = data?.hashValue ?: ">>")
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
