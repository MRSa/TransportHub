package net.osdn.ja.gokigen.transporthub.presentation.model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.osdn.ja.gokigen.transporthub.DbSingleton
import net.osdn.ja.gokigen.transporthub.storage.DataContent

class DataListModel : ViewModel()
{
    private var isRefreshing = false
    private val storageDao = DbSingleton.db.storageDao()
    val dataList = mutableStateListOf<DataContent>()

    init
    {
        update()
    }

    private fun update()
    {
        CoroutineScope(Dispatchers.Main).launch {
            if (!isRefreshing)
            {
                isRefreshing = true
                dataList.clear()
                withContext(Dispatchers.Default) {
                    storageDao.getAll().forEach { data ->
                        dataList.add(data)
                    }
                }
                isRefreshing = false
            }
        }
    }

    fun refresh()
    {
        try
        {
            if (!isRefreshing)
            {
                update()
                Log.v(TAG, "DATA REFRESHED : ${dataList.count()}")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = DataListModel::class.java.simpleName
    }
}