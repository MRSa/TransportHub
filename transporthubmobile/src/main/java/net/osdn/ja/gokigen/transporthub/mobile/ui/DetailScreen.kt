package net.osdn.ja.gokigen.transporthub.mobile.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.osdn.ja.gokigen.transporthub.mobile.ContentDataSender
import net.osdn.ja.gokigen.transporthub.mobile.DbSingleton
import net.osdn.ja.gokigen.transporthub.mobile.R
import net.osdn.ja.gokigen.transporthub.mobile.model.DetailModel
import net.osdn.ja.gokigen.transporthub.mobile.ui.theme.SubGreen

import net.osdn.ja.gokigen.transporthub.mobile.ui.theme.TransportHubTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DataDetail(context: Context, navController: NavHostController, id : Int)
{
    val model = DetailModel(id)

    TransportHubTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(4.dp)
            ) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                if (model.dataInitialized())
                {
                    val data = model.detailData
                    DetailScreenTitle(navController, data.title)
                    val receiveDateText = stringResource(id = R.string.received_date) + " ${model.dataContent?.receivedDate?.let { dateFormat.format(it) }}"
                    Text(
                        text = receiveDateText,
                        color = Color.LightGray,
                        textAlign = TextAlign.Right,
                        fontSize = 12.sp,
                    )
                    if ((model.dataContent != null)&&(model.dataContent?.sendDate != null))
                    {
                        val sendDateText = stringResource(id = R.string.send_date) + " ${model.dataContent?.sendDate?.let { dateFormat.format(it) }}"
                        Text(
                            text = sendDateText,
                            color = Color.LightGray,
                            textAlign = TextAlign.Right,
                            fontSize = 12.sp,
                        )
                    }
                    if ((model.dataContent != null)&&(model.dataContent?.sharedDate != null))
                    {
                        val sharedDateText = stringResource(id = R.string.shared_date) + " ${model.dataContent?.sharedDate?.let { dateFormat.format(it) }}"
                        Text(
                            text = sharedDateText,
                            color = Color.LightGray,
                            textAlign = TextAlign.Right,
                            fontSize = 12.sp,
                        )
                    }
                    ButtonArea(context, navController, model)
                    Text(
                        text = data.value,
                        //color = Color.LightGray,
                        fontSize = 14.sp,
                    )
                }
                else
                {
                    DetailScreenTitle(navController, "?")
                    ButtonArea(context, navController, model)
                    Text(
                        text = "??",
                        color = Color.LightGray,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun DetailScreenTitle(navController: NavHostController, title: String)
{
    TopAppBar(
        title = { Text(text = title) },
        modifier = Modifier.clickable(onClick = { navController.popBackStack() }),
        backgroundColor = Color(0xff3DDC84),
        contentColor = if (isSystemInDarkTheme()) { Color.Black } else { Color.White },
    )
}

@Composable
fun ButtonArea(context: Context, navController: NavHostController, model: DetailModel)
{
    val deleteDialog = remember { mutableStateOf(false) }
    Row {
        IconButton(
            onClick = {
                // Send data to Watch
                try
                {
                    // データをウオッチに送る
                    val sender = ContentDataSender(context)
                    sender.sendContent(model)
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_watch_24),
                contentDescription = "Send",
                tint = if(isSystemInDarkTheme()) { Color.LightGray } else { Color.DarkGray }
            )
        }
        IconButton(
            onClick = {
                // Issue Share Intent
                // Intent発行(ACTION_SEND)
                try
                {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND  // NoteIntents.ACTION_CREATE_NOTE
                        type = "text/plain" // MIME TYPE
                        putExtra(Intent.EXTRA_SUBJECT, model.detailData.title)
                        putExtra(Intent.EXTRA_TEXT, model.detailData.value)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    //val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(sendIntent)
                    Log.v("DetailData", "<<< SEND INTENT >>> : ${model.detailData.title}")

                    // 共有を実行した時刻を設定する
                    val thread = Thread {
                        val storageDao = DbSingleton.db.storageDao()
                        storageDao.updateSharedDate(model.id, Date())
                    }
                    try
                    {
                        thread.start()
                        Toast.makeText(context, context.getString(R.string.intent_issued), Toast.LENGTH_SHORT).show()  // UIスレッドで実行が必要
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
            },
            enabled = true
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = if(isSystemInDarkTheme()) { Color.LightGray } else { Color.DarkGray }
            )
        }
        IconButton(
            onClick = {
                // Delete Confirmation
                deleteDialog.value = true
            },
            enabled = true
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = if(isSystemInDarkTheme()) { Color.LightGray } else { Color.DarkGray }
            )
        }
        IconButton(
            onClick = { navController.popBackStack() },
            enabled = true
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = if(isSystemInDarkTheme()) { Color.LightGray } else { Color.DarkGray }
            )
        }
    }

    // データ削除の確認
    if (deleteDialog.value) {
        AlertDialog(
            onDismissRequest = {
                deleteDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteDialog.value = false
                        model.deleteContent()
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = SubGreen)
                ) {
                    Text(stringResource(id = R.string.delete_ok_label))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deleteDialog.value = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = SubGreen)
                ) {
                    Text(stringResource(id = R.string.delete_cancel_label))
                }
            },
            title = {
                Text(stringResource(id = R.string.delete_confirm_title))
            },
            text = {
                val message = stringResource(id = R.string.delete_confirm_message) + " \n " + model.detailData.title
                Text(message)
            },
        )
    }
}
