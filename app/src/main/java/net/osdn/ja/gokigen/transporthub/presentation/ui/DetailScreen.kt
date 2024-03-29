package net.osdn.ja.gokigen.transporthub.presentation.ui

import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.scrollAway
import kotlinx.coroutines.launch
import net.osdn.ja.gokigen.transporthub.ContentDataSender
import net.osdn.ja.gokigen.transporthub.DbSingleton
import net.osdn.ja.gokigen.transporthub.R
import net.osdn.ja.gokigen.transporthub.presentation.model.DetailModel
import net.osdn.ja.gokigen.transporthub.presentation.theme.GokigenComposeAppsTheme
import net.osdn.ja.gokigen.transporthub.presentation.theme.defaultColorPalette
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DataDetail(context: Context, navController: NavHostController, id : Int)
{
    val model = DetailModel(id)
    GokigenComposeAppsTheme {
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        Scaffold(
            timeText = {
                TimeText(
                    timeSource = TimeTextDefaults.timeSource(
                        DateFormat.getBestDateTimePattern(
                            Locale.getDefault(),
                            "HH:mm"
                        ),
                    ),
                    modifier = Modifier.scrollAway(scrollState = scrollState)
                ) },
            positionIndicator = {
                PositionIndicator(scrollState = scrollState)
            },
        ) {
            Column(
                modifier = Modifier
                    .onRotaryScrollEvent {
                        coroutineScope.launch {
                            scrollState.scrollBy(it.verticalScrollPixels)
                        }
                        true
                    }
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .focusRequester(focusRequester)
                    .focusable()

            ) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                if (model.dataInitialized()) {
                    val data = model.detailData
                    DetailScreenTitle(navController, data.title)
                    val receiveDateText = stringResource(id = R.string.received_date) + " ${
                        model.dataContent?.receivedDate?.let {
                            dateFormat.format(it)
                        }
                    }"
                    Text(
                        text = receiveDateText,
                        color = defaultColorPalette.onSurfaceVariant,
                        fontSize = 11.sp,
                    )
                    if ((model.dataContent != null) && (model.dataContent?.sendDate != null)) {
                        val sendDateText = stringResource(id = R.string.send_date) + " ${
                            model.dataContent?.sendDate?.let {
                                dateFormat.format(it)
                            }
                        }"
                        Text(
                            text = sendDateText,
                            color = defaultColorPalette.onSurfaceVariant,
                            fontSize = 11.sp,
                        )
                    }
                    if ((model.dataContent != null) && (model.dataContent?.sharedDate != null)) {
                        val sharedDateText = stringResource(id = R.string.shared_date) + " ${
                            model.dataContent?.sharedDate?.let {
                                dateFormat.format(it)
                            }
                        }"
                        Text(
                            text = sharedDateText,
                            color = defaultColorPalette.onSurfaceVariant,
                            fontSize = 11.sp,
                        )
                    }
                    ButtonArea(context, navController, model)
                    Text(
                        text = data.value,
                        color = Color.LightGray,
                        fontSize = 12.sp,
                    )
                } else {
                    DetailScreenTitle(navController, "?")
                    ButtonArea(context, navController, model)
                    Text(
                        text = "??",
                        color = Color.LightGray,
                        fontSize = 12.sp,
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
        modifier = Modifier
            .clickable(onClick = { navController.popBackStack() }),
        contentPadding = PaddingValues(
            top = 0.dp,
            start = 16.dp, // 16.dp
            end = 16.dp,   // 16.dp
            bottom = 0.dp,
        ),
        backgroundColor = Color(0x00000000),  // タイトル部分の背景は透過
        ) {
            Text(
                text = title,
                color = defaultColorPalette.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
            )
    }
}

@Composable
fun ButtonArea(context: Context, navController: NavHostController, model: DetailModel)
{
    val deleteDialog = remember { mutableStateOf(false) }
    Row {
        IconButton(
            onClick = {
                try
                {
                    // Send to other devices.
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
                painter = painterResource(id = R.drawable.baseline_send_to_mobile_24),
                contentDescription = "Send",
                tint = Color.LightGray
            )
        }
        IconButton(
            onClick = {
                try
                {
                    // Share the content.
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND  // NoteIntents.ACTION_CREATE_NOTE
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, model.detailData.title)
                        putExtra(Intent.EXTRA_TEXT, model.detailData.value)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
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
                tint = Color.LightGray
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
                tint = Color.LightGray
            )
        }
        IconButton(
            onClick = { navController.popBackStack() },
            enabled = true
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.LightGray
            )
        }
    }

    // データ削除の確認
    if (deleteDialog.value)
    {
        AlertDialog(
            modifier = Modifier.fillMaxSize()
                .padding(top = 20.dp, bottom = 0.dp, start = 4.dp, end = 12.dp),
            contentColor = defaultColorPalette.onSurface,
            onDismissRequest = {
                deleteDialog.value = false
            },
            backgroundColor = defaultColorPalette.background,
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteDialog.value = false
                        model.deleteContent()
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.delete_ok_label),
                        color = defaultColorPalette.secondary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deleteDialog.value = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.delete_cancel_label),
                        color = defaultColorPalette.secondary
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(id = R.string.delete_confirm_title),
                    //color = defaultColorPalette.onSurface
                )
            },
            text = {
                //val message = stringResource(id = R.string.delete_confirm_message) + " \n " + model.detailData.title
                val message = " " + model.detailData.title
                Text(
                    text = message,
                    //color = defaultColorPalette.onSurface
                )
            },
        )
    }
}
