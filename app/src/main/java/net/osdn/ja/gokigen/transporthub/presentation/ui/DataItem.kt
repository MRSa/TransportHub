package net.osdn.ja.gokigen.transporthub.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import net.osdn.ja.gokigen.transporthub.R
import net.osdn.ja.gokigen.transporthub.presentation.theme.defaultColorPalette
import net.osdn.ja.gokigen.transporthub.storage.DataContent
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DataItem(navController: NavHostController, data: DataContent)
{
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    val dataId = data.id

    // アイコンの色を決定するヘルパー関数
    val iconTint = remember(data.sendDate, data.sharedDate) {
        when {
            data.sendDate == null && data.sharedDate == null -> Color.DarkGray // send: no, share: no
            data.sendDate == null && data.sharedDate != null -> Color(48, 182, 127) // send: no, share: yes
            data.sendDate != null && data.sharedDate == null -> Color(215, 182, 61) // send: yes, share: no
            else -> Color.LightGray // send: yes, share: yes
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth() // Row全体をクリック可能にする
            .clickable(onClick = { navController.navigate("DetailScreen/$dataId") })
            .padding(4.dp) // Rowにパディングを移動
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.check_icon_description), // contentDescriptionも文字列リソース化
            tint = iconTint,
            modifier = Modifier.size(24.dp) // アイコンのサイズを明示的に指定
        )
        Spacer(modifier = Modifier.width(8.dp)) // アイコンとテキストの間にスペースを追加

        Column(
            modifier = Modifier.weight(1f) // 残りのスペースを埋める
        ) {
            data.title?.let {
                Text(
                    fontSize = 14.sp,
                    text = it,
                    color = defaultColorPalette.primary,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp)
                )
            }
            val formattedReceivedDate = data.receivedDate?.let { dateFormat.format(it) } ?: ""
            Text(
                text = stringResource(R.string.received_date_format, formattedReceivedDate), // プレースホルダーを使用
                fontSize = 12.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
