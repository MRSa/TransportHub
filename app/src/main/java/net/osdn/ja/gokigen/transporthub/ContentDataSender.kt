package net.osdn.ja.gokigen.transporthub

import android.content.Context
import android.os.Parcel
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import net.osdn.ja.gokigen.transporthub.presentation.model.DetailModel
import java.lang.Exception

class ContentDataSender(val context: Context)
{

    fun sendContent(model: DetailModel)
    {
        try
        {
            Log.v(TAG, "sendContent : ${model.detailData.title}")
            val thread = Thread {
                val nodeListTask: Task<List<Node>> = Wearable.getNodeClient(context).connectedNodes
                val nodes: List<Node> = Tasks.await(nodeListTask)
                for (node in nodes)
                {
                    val messageClient: MessageClient = Wearable.getMessageClient(context)

                    //  ----- データをシリアライズして送信する ----
                    val parcel = Parcel.obtain()
                    model.detailData.writeToParcel(parcel, 0)
                    val detailDataBytes = parcel.marshall()
                    parcel.recycle()

                    val clientTask = messageClient.sendMessage(
                        node.id,
                        "message_transfer",
                        detailDataBytes
                    )
                    try
                    {
                        val result = Tasks.await(clientTask)
                        Log.v(TAG, " sent : '$model.title'  result:$result")
                    }
                    catch (ee: Exception)
                    {
                        ee.printStackTrace()
                    }
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = MainActivity::class.java.simpleName
    }
}
