package net.osdn.ja.gokigen.transporthub.mobile.service

import android.os.Parcel
import android.util.Log
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.parcelize.parcelableCreator
import net.osdn.ja.gokigen.transporthub.mobile.DbSingleton
import net.osdn.ja.gokigen.transporthub.mobile.model.DetailData
import net.osdn.ja.gokigen.transporthub.mobile.storage.DataContent
import java.security.MessageDigest

class ListenerService : WearableListenerService()
{
    private var isRefreshing = false

    override fun onCapabilityChanged(capability: CapabilityInfo)
    {
        super.onCapabilityChanged(capability)
        Log.v(TAG, " ===== onCapabilityChanged : ${capability.name} / ${capability.nodes}")
    }

    override fun onMessageReceived(messsage: MessageEvent)
    {
        super.onMessageReceived(messsage)
        Log.v(TAG, "onMessageReceived")
        try
        {
            val thread = Thread {
                if (!isRefreshing)
                {
                    isRefreshing = true

                    val parcel = Parcel.obtain()
                    parcel.unmarshall(messsage.data, 0, messsage.data.size)
                    parcel.setDataPosition(0)
                    val detailData = parcelableCreator<DetailData>().createFromParcel(parcel)
                    parcel.recycle()

                    val title = detailData.title
                    val data = detailData.value
                    val checkString:String = title + data
                    val md = MessageDigest.getInstance("SHA-256")
                    val digest = md.digest(checkString.toByteArray()).toString()
                    Log.v(TAG, " <><><><><><> INSERT (Title: $title , DIGEST: $digest) Length: ${checkString.length}")

                    // いちおう本文とタイトルのハッシュで既に登録済か確認する
                    val storageDao = DbSingleton.db.storageDao()
                    val check = storageDao.findByHash(digest)
                    if (check.isEmpty())
                    {
                        // データが入っていないので、データベースに登録する
                        val content = DataContent.create(title, digest, data)
                        storageDao.insertAll(content)
                    }
                    else
                    {
                        Log.v(TAG, " ===== DATA CONTENT IS ALREADY REGISTERED =====")
                    }
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

    companion object
    {
        private val TAG = ListenerService::class.java.simpleName
    }
}
