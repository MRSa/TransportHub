package net.osdn.ja.gokigen.transporthub.mobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import kotlinx.parcelize.parcelableCreator
import net.osdn.ja.gokigen.transporthub.mobile.model.DetailData
import net.osdn.ja.gokigen.transporthub.mobile.storage.DataContent
import net.osdn.ja.gokigen.transporthub.mobile.storage.DataContentDao
import net.osdn.ja.gokigen.transporthub.mobile.ui.ViewRoot
import java.io.File
import java.security.MessageDigest

class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener
{
    private var isRefreshing = false
    private lateinit var rootComponent : ViewRoot
    private val storageDao = DbSingleton.db.storageDao()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        try
        {
            ///////// SHOW SPLASH SCREEN : call before super.onCreate() /////////
            installSplashScreen()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        super.onCreate(savedInstanceState)

        try
        {
            if (!allPermissionsGranted())
            {
                val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                    if(!allPermissionsGranted())
                    {
                        Log.v(TAG, "permission result: ${result.count()}")
                        Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                requestPermission.launch(REQUIRED_PERMISSIONS)
            }
            else
            {
                setupEnvironments()
            }
            rootComponent = ViewRoot(applicationContext)
            setContent {
                rootComponent.Content()
            }
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }

    override fun onPause()
    {
        super.onPause()
        Log.v(TAG, "onPause()")
        try
        {
            Wearable.getMessageClient(this).removeListener(this)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onResume()
    {
        super.onResume()
        Log.v(TAG, "onResume()")
        try
        {
            Wearable.getMessageClient(this).addListener(this)
            Log.v(TAG, " - - - Add Listener - - -")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupEnvironments()
    {
        val thread = Thread {
            try
            {
                val subDirectory = File(filesDir, ATTACHMENT_DIR)
                if (!subDirectory.exists())
                {
                    if (!subDirectory.mkdirs())
                    {
                        Log.v(TAG, "----- Sub Directory $ATTACHMENT_DIR create failure...")
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }

            val contents: List<DataContent> = storageDao.getAll()
            Log.v(TAG, " = = = = = number of contents : ${contents.count()} = = = = =")
            if (isDebugLog)
            {
                var index = 1
                for (value in contents)
                {
                    Log.v(TAG, "    $index title:${value.title} hash:${value.hashValue}")
                    index++
                }
            }

            // Process the received Intent
            if (intent?.action == Intent.ACTION_SEND)
            {
                Log.v(TAG, "Received Intent")
                handleReceivedIntent(intent, storageDao)
            }
        }
        try
        {
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onMessageReceived(message: MessageEvent)
    {
        Log.v(TAG, "MainActivity::onMessageReceived")
        try
        {
            val thread = Thread {
                if (!isRefreshing)
                {
                    isRefreshing = true

                    val parcel = Parcel.obtain()
                    parcel.unmarshall(message.data, 0, message.data.size)
                    parcel.setDataPosition(0)
                    val detailData = parcelableCreator<DetailData>().createFromParcel(parcel)
                    parcel.recycle()

                    val title = detailData.title
                    val data = detailData.value
                    val checkString:String = title + data
                    val md = MessageDigest.getInstance("MD5")
                    val digestArray = md.digest(checkString.toByteArray())
                    var digest = ""
                    digestArray.forEach { digest += "%02x".format(it) }

                    Log.v(TAG, " ========== INSERT (Title: $title , DIGEST: $digest) Length: ${checkString.length}")

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
                        Log.v(TAG, " ----- DATA CONTENT IS ALREADY REGISTERED -----")
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

    private fun handleReceivedIntent(intent: Intent, dao: DataContentDao)
    {
        try
        {
            val title = intent.getStringExtra(Intent.EXTRA_SUBJECT)
            val data = intent.getStringExtra(Intent.EXTRA_TEXT)
            val checkString:String = title + data
            // Intent.EXTRA_STREAM の処理を行っていない

            val md = MessageDigest.getInstance("MD5")
            val digestArray = md.digest(checkString.toByteArray())
            var digest = ""
            digestArray.forEach { digest += "%02x".format(it) }
            Log.v(TAG, "RECEIVED INTENT (Title: $title , DIGEST: $digest) Length: ${checkString.length}")

            // いちおう本文とタイトルのハッシュで既に登録済か確認する
            val check = dao.findByHash(digest)
            if (check.isEmpty())
            {
                // データが入っていないので、データベースに登録する
                val content = DataContent.create(title, digest, data)
                dao.insertAll(content)
            }
            else
            {
                Log.v(TAG, " ===== DATA CONTENT IS ALREADY REGISTERED =====")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = MainActivity::class.java.simpleName
        private const val ATTACHMENT_DIR : String = "/attaches/"
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.VIBRATE,
        )
        private const val isDebugLog = true
    }
}
