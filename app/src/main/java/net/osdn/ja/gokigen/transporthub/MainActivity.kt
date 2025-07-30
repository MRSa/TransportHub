package net.osdn.ja.gokigen.transporthub

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import net.osdn.ja.gokigen.transporthub.presentation.ui.ViewRoot
import net.osdn.ja.gokigen.transporthub.storage.DataContent
import net.osdn.ja.gokigen.transporthub.storage.DataContentDao
import java.io.File
import java.security.MessageDigest

class MainActivity : ComponentActivity()
{
    private lateinit var rootComponent : ViewRoot
    private val storageDao = DbSingleton.db.storageDao()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        try
        {
            ///////// SHOW SPLASH SCREEN : call before super.onCreate() /////////
            installSplashScreen()

            enableEdgeToEdge()
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
                val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    if(!allPermissionsGranted())
                    {
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
                val flags = intent.flags
                val checkFlags = Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
                if ((flags.and(checkFlags)) == 0)
                {
                    Log.v(TAG, " DATA IMPORT ")
                    handleReceivedIntent(intent, storageDao)
                }
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

    private fun handleReceivedIntent(intent: Intent, dao: DataContentDao)
    {
        try
        {
            val md = MessageDigest.getInstance("MD5")
            val titleData = intent.getStringExtra(Intent.EXTRA_SUBJECT)
            val data = intent.getStringExtra(Intent.EXTRA_TEXT)

            val title = if (titleData == null) {
                // タイトルが空白だった場合は、本文から切り出しを行う
                var index = data?.indexOf(System.lineSeparator()) ?: 0
                if ((index > 50)||(index <= 0))
                {
                    index = 50
                }
                data?.substring(0, index)
            }
            else
            {
                titleData
            }

            val checkString = title + data
            val digestArray = md.digest(checkString.toByteArray())

            var digest = ""
            digestArray.forEach { digest += "%02x".format(it) }

            Log.v(TAG, "RECEIVED INTENT (Title: $title , DIGEST: ${digestArray.size} ${md.digestLength}) Length: ${checkString.length}")

            // いちおう本文とタイトルのハッシュで既に登録済か確認する
            var message = getString(R.string.data_imported) + title
            val check = dao.findByHash(digest)
            if (check.isEmpty())
            {
                // データが入っていないので、データベースに登録する
                val content = DataContent.create(title, digest, data)
                dao.insertAll(content)
            }
            else
            {
                message = getString(R.string.data_not_import) + title
                Log.v(TAG, " ===== DATA CONTENT IS ALREADY REGISTERED : IGNORE =====")
            }
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                finish()
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
            Manifest.permission.WAKE_LOCK,
        )
        private const val isDebugLog = true
    }
}
