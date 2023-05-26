package net.osdn.ja.gokigen.transporthub.presentation.model


import net.osdn.ja.gokigen.transporthub.DbSingleton

class DetailModel(val id: Int)
{
    var value:String = ""
    var title: String = "($id)"

    init
    {
        update()
    }

    fun update()
    {
        try
        {
            val thread = Thread {
                val storageDao = DbSingleton.db.storageDao()
                val data = storageDao.findById(id)
                value = data.note?: "???"
                title = data.title?: "?"
                //Log.v("DataDetail", "DataDetail($id)\n$value")
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}
