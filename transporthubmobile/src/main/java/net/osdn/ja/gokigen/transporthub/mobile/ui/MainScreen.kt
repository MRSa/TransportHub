package net.osdn.ja.gokigen.transporthub.mobile.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.osdn.ja.gokigen.transporthub.mobile.R
import net.osdn.ja.gokigen.transporthub.mobile.model.DataListModel
import net.osdn.ja.gokigen.transporthub.mobile.ui.theme.TransportHubTheme

@Composable
fun WearApp(navController: NavHostController, dataListModel: DataListModel)
{
    dataListModel.refresh()
    TransportHubTheme {
        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
