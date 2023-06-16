package net.osdn.ja.gokigen.transporthub.presentation.ui

import android.text.format.DateFormat
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import kotlinx.coroutines.launch
import net.osdn.ja.gokigen.transporthub.R
import net.osdn.ja.gokigen.transporthub.presentation.model.DataListModel
import net.osdn.ja.gokigen.transporthub.presentation.theme.GokigenComposeAppsTheme
import net.osdn.ja.gokigen.transporthub.presentation.theme.defaultColorPalette
import java.util.Locale

@Composable
fun WearApp(navController: NavHostController, dataListModel: DataListModel)
{
    dataListModel.refresh()

    GokigenComposeAppsTheme {
        val listState = rememberScalingLazyListState()
        TimeText(
            timeSource = TimeTextDefaults.timeSource(
                DateFormat.getBestDateTimePattern(
                    Locale.getDefault(),
                    "HH:mm"
                )
            )
        )
        Scaffold(
            positionIndicator = {
                PositionIndicator(scalingLazyListState = listState)
            },
        ) {
            val focusRequester = remember { FocusRequester() }
            val coroutineScope = rememberCoroutineScope()
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize()
                    .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                    }
                        true
                    }
                    .focusRequester(focusRequester)
                    .focusable(),
                contentPadding = PaddingValues(
                    top = 32.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 32.dp,
                ),
                verticalArrangement = Arrangement.Center,
                state = listState
            ) {
                this.items(dataListModel.dataList) { data ->
                    key(data.id) {
                        DataItem(navController, data)
                    }
                }
            }
            if (dataListModel.dataList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Text(
                        text = stringResource(id = R.string.data_empty),
                        color = defaultColorPalette.primary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                    )

                }
            }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        }
    }
}
