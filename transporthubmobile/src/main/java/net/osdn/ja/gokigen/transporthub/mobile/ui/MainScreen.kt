package net.osdn.ja.gokigen.transporthub.mobile.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.osdn.ja.gokigen.transporthub.mobile.R
import net.osdn.ja.gokigen.transporthub.mobile.model.DataListModel
import net.osdn.ja.gokigen.transporthub.mobile.ui.theme.TransportHubTheme

@Composable
fun MobileApp(navController: NavHostController, dataListModel: DataListModel)
{
    dataListModel.refresh()
    TransportHubTheme {
        Scaffold(
            topBar = { MainTopBar(navController) },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
        ) {
            Modifier.padding(it).fillMaxWidth()
            ReceivedContentList(navController,dataListModel)
        }
    }
}

@Composable
fun MainTopBar(navController: NavHostController)
{
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MRSa/GokigenOSDN_documents/blob/main/Applications/TransportHub/Readme.md")) }
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        backgroundColor = Color(0xff3DDC84),
        contentColor = if (isSystemInDarkTheme()) { Color.Black } else { Color.White },
        actions = {
            IconButton(onClick = { navController.navigate("PreferenceScreen") }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
            IconButton(
                onClick = { context.startActivity(intent) /* Open the Web Page */ })
            {
                Icon(Icons.Filled.Info, contentDescription = "Information")
            }
        }
    )
}

@Composable
fun ReceivedContentList(navController: NavHostController, dataListModel: DataListModel)
{
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
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
