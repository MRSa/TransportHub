package net.osdn.ja.gokigen.transporthub.presentation.ui

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            if (model.dataInitialized())
            {
                val data = model.detailData
                DetailScreenTitle(navController, data.title)
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
    if (deleteDialog.value) {
        AlertDialog(
            modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 6.dp, end = 6.dp),
            onDismissRequest = {
                deleteDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteDialog.value = false
                        model.deleteContent()
                        navController.navigate("MainScreen")
                    }
                ) {
                    Text(stringResource(id = R.string.delete_ok_label))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deleteDialog.value = false
                    }
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
