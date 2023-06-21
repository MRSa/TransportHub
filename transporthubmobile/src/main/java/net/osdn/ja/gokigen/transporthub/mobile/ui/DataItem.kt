package net.osdn.ja.gokigen.transporthub.mobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.osdn.ja.gokigen.transporthub.mobile.R
import net.osdn.ja.gokigen.transporthub.mobile.storage.DataContent
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
            tint =
            if (data.sendDate == null)
            {
               if(isSystemInDarkTheme()) { Color.DarkGray } else { Color.LightGray }
            }
            else
            {
                if(isSystemInDarkTheme()) { Color.LightGray } else { Color.DarkGray }
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
                    fontSize = 16.sp,
                    text = it,
                    color = if(isSystemInDarkTheme()) { Color.LightGray } else { Color.DarkGray },
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
                fontSize = 14.sp,
                color = if(isSystemInDarkTheme()) { Color.LightGray } else { Color.DarkGray },
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
