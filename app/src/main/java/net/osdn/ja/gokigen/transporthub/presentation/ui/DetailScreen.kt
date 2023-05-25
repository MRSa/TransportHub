package net.osdn.ja.gokigen.transporthub.presentation.ui

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import net.osdn.ja.gokigen.transporthub.DbSingleton
import net.osdn.ja.gokigen.transporthub.R
import net.osdn.ja.gokigen.transporthub.presentation.theme.GokigenComposeAppsTheme
import net.osdn.ja.gokigen.transporthub.presentation.theme.wearColorPalette
import java.util.Locale

@Composable
fun DataDetail(navController: NavHostController, id : Int)
{
    var value = ""
    try
    {
        val thread = Thread {
            val storageDao = DbSingleton.db.storageDao()
            val data = storageDao.findById(id)
            value = data.note?: "???"
            //Log.v("DataDetail", "DataDetail($id)\n$value")
        }
        thread.start()
    }
    catch (e: Exception)
    {
        e.printStackTrace()
    }

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
            DetailScreenTitle(navController)
            Text(
                text = value,
                color = wearColorPalette.primaryVariant,
                fontSize = 12.sp,
            )
            ShowGokigenPrivacyPolicy()
        }
    }
}

@Composable
fun DetailScreenTitle(navController: NavHostController) {
    TopAppBar(
        modifier = Modifier
            .padding(22.dp)
            .clickable(onClick = { navController.navigate("MainScreen") })
    ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = wearColorPalette.primary,
                fontSize = 12.sp,
            )
    }
}

@Composable
fun ShowGokigenPrivacyPolicy()
{
    val density = LocalDensity.current
    val uriHandler = LocalUriHandler.current
    val openUri = stringResource(R.string.pref_privacy_policy_url)
    Row(modifier = Modifier.padding(all = 2.dp)) {
        Spacer(modifier = Modifier.width(2.dp))
        Column {
            Text(
                text = stringResource(R.string.pref_privacy_policy),
                color = wearColorPalette.onSurfaceVariant,
                fontSize = with(density) { 12.sp }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = openUri,
                color = wearColorPalette.secondaryVariant,
                modifier = Modifier.clickable(onClick = { uriHandler.openUri(openUri) }),
                fontSize = with(density) { 10.sp }
            )
        }
    }
}
