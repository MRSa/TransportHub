package net.osdn.ja.gokigen.transporthub.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
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
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val dataId = data.id
    Row()
    {
        Icon(
            modifier =  Modifier
                .clickable(onClick = { navController.navigate("DetailScreen/$dataId") }),
            imageVector = Icons.Default.Check,
            contentDescription = "Check",
            tint = if (data.sendDate == null)
            {
                if (data.sharedDate == null)
                {
                    // send: no, share: no  (dark gray)
                    Color.DarkGray
                }
                else
                {
                    // send: no, share: yes
                    Color(48, 182, 127)
                }
            }
            else
            {
                if (data.sharedDate == null)
                {
                    // send: yes, share: no
                    Color(215, 182, 61)
                }
                else
                {
                    // send: yes, share: yes (Light Gray)
                    Color.LightGray
                }
            },
            )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable(onClick = { navController.navigate("DetailScreen/$dataId") })
        ) {
            data.title?.let {
                Text(
                    fontSize = 14.sp,
                    text = it,
                    color = defaultColorPalette.primary,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp)
                )
            }
            Text(
                //text =  " ${data.receivedDate?.let { dateFormat.format(it) }}",
                text = stringResource(R.string.received_date) + " ${
                    data.receivedDate?.let {
                        dateFormat.format(
                            it
                        )
                    }
                }",
                fontSize = 12.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
