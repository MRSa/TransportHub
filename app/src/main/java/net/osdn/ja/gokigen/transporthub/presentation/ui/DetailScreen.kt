package net.osdn.ja.gokigen.transporthub.presentation.ui

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.osdn.ja.gokigen.transporthub.DbSingleton
import net.osdn.ja.gokigen.transporthub.R
import net.osdn.ja.gokigen.transporthub.presentation.model.DetailModel
import net.osdn.ja.gokigen.transporthub.presentation.theme.GokigenComposeAppsTheme
import net.osdn.ja.gokigen.transporthub.presentation.theme.wearColorPalette
import java.util.Locale

@Composable
fun DataDetail(navController: NavHostController, id : Int)
{
    val model = DetailModel(id)
    model.update()

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
            DetailScreenTitle(navController, model.title)
            ButtonArea(navController, model)
            Text(
                text = model.value,
                color = wearColorPalette.primaryVariant,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun DetailScreenTitle(navController: NavHostController, title: String) {
    TopAppBar(
        modifier = Modifier
            .padding(0.dp)
            .clickable(onClick = { navController.navigate("MainScreen") })
    ) {
            Text(
                text = title,
                color = wearColorPalette.primary,
                fontSize = 12.sp,
            )
    }
}

@Composable
fun ButtonArea(navController: NavHostController, model: DetailModel)
{
    Row {
        IconButton(
            onClick = {  },
            enabled = true
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.LightGray
            )
        }
        IconButton(
            onClick = {  },
            enabled = true
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.LightGray
            )
        }
/*
        IconButton(
            onClick = { model.update() },
            enabled = true
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = Color.LightGray
            )
        }
*/
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
}

