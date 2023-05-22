package net.osdn.ja.gokigen.transporthub.presentation.ui

import android.text.format.DateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import net.osdn.ja.gokigen.transporthub.presentation.theme.GokigenComposeAppsTheme
import net.osdn.ja.gokigen.transporthub.storage.DataContent
import java.util.Locale

@Composable
fun WearApp(title: String, dataList: SnapshotStateList<DataContent>) {
    GokigenComposeAppsTheme {

        val listState = rememberLazyListState()

        Column(
            modifier = Modifier
                //.fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            TimeText(timeSource = TimeTextDefaults.timeSource(DateFormat.getBestDateTimePattern(Locale.getDefault(), "HH:mm")))
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(dataList) { data ->
                    key(data.id) {
                        DataItem(data)
                        Log.v("TAG", " data: $data.title")
                    }
                }
            }
        }

        // Modifiers used by our Wear composables.
        val contentModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
        val iconModifier = Modifier
            .size(24.dp)
            .wrapContentSize(align = Alignment.Center)



        /* *************************** Part 3: ScalingLazyColumn *************************** */
        // TODO: Swap a ScalingLazyColumn (Wear's version of LazyColumn)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 32.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.Center,
            state = listState
        ) {

                items(dataList) { data ->
                    key(data.id) {
                        DataItem(data)
                        Log.v("TAG", " data: $data.title")
                    }
            }
        }
/*
        Text(text = stringResource(R.string.tile_label))
        Text(text = stringResource(R.string.tile_label))

        // TODO (End): Create a Scaffold (Wear Version)
*/


/*
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {

            TimeText(
                timeSource = TimeTextDefaults.timeSource(
                    DateFormat.getBestDateTimePattern(Locale.getDefault(), "HH:mm")
                )
            )

            Text(text = stringResource(R.string.tile_label))
            Text(text = stringResource(R.string.tile_label))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            TimeText(
                timeSource = TimeTextDefaults.timeSource(
                    DateFormat.getBestDateTimePattern(Locale.getDefault(), "HH:mm")
                )
            )
            Text(text = stringResource(R.string.tile_label))
        }
*/
    }
}
