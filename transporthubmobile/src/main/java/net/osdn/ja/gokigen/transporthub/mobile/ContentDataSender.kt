package net.osdn.ja.gokigen.transporthub.mobile

import android.content.Context
import android.os.Parcel
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import net.osdn.ja.gokigen.transporthub.mobile.model.DetailModel
import java.lang.Exception
import java.util.Date
import java.util.concurrent.TimeUnit

class ContentDataSender(val context: Context)
{
    fun sendContent(model: DetailModel)
    {
        try
        {
            Log.v(TAG, "---------- sendContent : '${model.detailData.title}'")
            val thread = Thread {
                try
                {
                    val nodeListTask: Task<List<Node>> = Wearable.getNodeClient(context).connectedNodes
                    val myNodeTask: Task<Node> = Wearable.getNodeClient(context).localNode
                    val myNode: Node = Tasks.await(myNodeTask)
                    val nodes: List<Node> = Tasks.await(nodeListTask)
                    var sentCount = 0
                    for (node in nodes)
                    {
                        if (node == myNode)
                        {
                            //  ----- 自ノードの場合は、送信しない -----
                            Log.v(TAG, " - - - My Node: ${myNode.displayName}")
                            continue
                        }
                        val messageClient: MessageClient = Wearable.getMessageClient(context)

                        //  ----- データをシリアライズして送信する ----
                        val parcel = Parcel.obtain()
                        model.detailData.writeToParcel(parcel, 0)
                        val detailDataBytes = parcel.marshall()
                        parcel.recycle()

                        val clientTask = messageClient.sendMessage(
                            node.id,
                            "/message_transfer",
                            detailDataBytes
                        ).apply {
                            addOnSuccessListener {
                                Log.v(TAG, "Transport Success : ${node.displayName}")
                                val thread = Thread {
                                    val storageDao = DbSingleton.db.storageDao()
                                    storageDao.updateSendDate(model.id, Date())
                                }
                                try
                                {
                                    thread.start()
                                    Toast.makeText(context, context.getString(R.string.data_transferred), Toast.LENGTH_SHORT).show()  // UIスレッドで実行が必要
                                    //Toast.makeText(context, context.getString(R.string.data_transferred) + " : " + node.displayName, Toast.LENGTH_SHORT).show()  // UIスレッドで実行が必要
                                }
                                catch (ee: Exception)
                                {
                                    ee.printStackTrace()
                                }
                            }
                            addOnFailureListener { Log.v(TAG, "Transport Failure : ${node.displayName}") }
                        }
                        try
                        {
                            val result = Tasks.await(clientTask, TIMEOUT, TimeUnit.SECONDS)
                            Log.v(TAG, " sent : '${model.detailData.title}' (${node.displayName}) result:$result bytes:${detailDataBytes.size}")
                            sentCount++
                        }
                        catch (ee: Exception)
                        {
                            ee.printStackTrace()
                        }
                    }
                    Log.v(TAG, "---------- sendContent : $sentCount")
                    System.gc()
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = ContentDataSender::class.java.simpleName
        private val TIMEOUT : Long = 10
    }
}
