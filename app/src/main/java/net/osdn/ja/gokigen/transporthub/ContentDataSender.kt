package net.osdn.ja.gokigen.transporthub

import android.content.Context
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
            val thread = Thread {
                val nodeListTask: Task<List<Node>> = Wearable.getNodeClient(context).connectedNodes
                val nodes: List<Node> = Tasks.await(nodeListTask)
                for (node in nodes) {
                    val messageClient: MessageClient = Wearable.getMessageClient(context)

                    //  ----- データはシリアライズして送るべき ----
                    val clientTask = messageClient.sendMessage(
                        node.getId(),
                        model.title,
                        model.value.toByteArray()
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
