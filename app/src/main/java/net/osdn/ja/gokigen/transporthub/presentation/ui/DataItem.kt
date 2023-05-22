package net.osdn.ja.gokigen.transporthub.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import net.osdn.ja.gokigen.transporthub.storage.DataContent
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DataItem(data: DataContent)
{
    val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = { }) //deleteData(data) })
    ) {
        data.title?.let {
            Text(
                text = it,
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
            )
        }
        Text(
            text = "created at: ${data.receivedDate?.let { dateFormat.format(it) }}",
            fontSize = 12.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth()
        )
    }



}