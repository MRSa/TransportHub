package net.osdn.ja.gokigen.transporthub.presentation.ui

import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import net.osdn.ja.gokigen.transporthub.ContentDataSender
import net.osdn.ja.gokigen.transporthub.R
import net.osdn.ja.gokigen.transporthub.presentation.model.DetailModel
import net.osdn.ja.gokigen.transporthub.presentation.theme.GokigenComposeAppsTheme
import net.osdn.ja.gokigen.transporthub.presentation.theme.wearColorPalette
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DataDetail(context: Context, navController: NavHostController, id : Int)
{
    val model = DetailModel(id)

    GokigenComposeAppsTheme {
        TimeText(
            timeSource = TimeTextDefaults.timeSource(
                DateFormat.getBestDateTimePattern(
                    Locale.getDefault(),
                    "HH:mm"
                )
            )
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 2.dp)
        ) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            if (model.dataInitialized())
            {
                val data = model.detailData
                DetailScreenTitle(navController, data.title)
                val receiveDateText = stringResource(id = R.string.received_date) + " ${model.dataContent?.receivedDate?.let { dateFormat.format(it) }}"
                Text(
                    text = receiveDateText,
                    color = wearColorPalette.onSurfaceVariant,
                    fontSize = 11.sp,
                )
                if ((model.dataContent != null)&&(model.dataContent?.sendDate != null))
                {
                    val sendDateText = stringResource(id = R.string.send_date) + " ${model.dataContent?.sendDate?.let { dateFormat.format(it) }}"
                    Text(
                        text = sendDateText,
                        color = wearColorPalette.onSurfaceVariant,
                        fontSize = 11.sp,
                    )
                }
                ButtonArea(context, navController, model)
                Text(
                    text = data.value,
                    color = wearColorPalette.primaryVariant,
                    fontSize = 12.sp,
                )
            }
            else
            {
                DetailScreenTitle(navController, "?")
                ButtonArea(context, navController, model)
                Text(
                    text = "??",
                    color = wearColorPalette.primaryVariant,
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
fun DetailScreenTitle(navController: NavHostController, title: String)
{
    TopAppBar(
        modifier = Modifier
            .clickable(onClick = { navController.navigate("MainScreen") }),
        contentPadding = PaddingValues(
            top = 0.dp,
            start = 16.dp, // 16.dp
            end = 16.dp,   // 16.dp
            bottom = 0.dp,
        ),
    ) {
            Text(
                text = title,
                color = wearColorPalette.primary,
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

                    // UIスレッドで実行が必要
                    Toast.makeText(context, context.getString(R.string.intent_issued), Toast.LENGTH_SHORT).show()
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
            onClick = { navController.navigate("MainScreen") },
            enabled = true
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.LightGray
            )
        }
    }

    // データ削除の確認
    if (deleteDialog.value)
    {
        AlertDialog(
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp),
            onDismissRequest = {
                deleteDialog.value = false
            },
            backgroundColor = wearColorPalette.onBackground,
            confirmButton = {
                Button(
                    onClick = {
                        deleteDialog.value = false
                        model.deleteContent()
                        navController.navigate("MainScreen")
                    }
                ) {
                    Text(text = stringResource(id = R.string.delete_ok_label))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        deleteDialog.value = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.delete_cancel_label))
                }
            },
            title = {
                Text(
                    text = stringResource(id = R.string.delete_confirm_title),
                    color = wearColorPalette.primary)
            },
            text = {
                //val message = stringResource(id = R.string.delete_confirm_message) + " \n " + model.detailData.title
                val message = " " + model.detailData.title
                Text(text = message, color = wearColorPalette.primary)
            },
        )
    }
}
