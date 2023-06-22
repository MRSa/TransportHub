package net.osdn.ja.gokigen.transporthub.mobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.osdn.ja.gokigen.transporthub.mobile.R
import net.osdn.ja.gokigen.transporthub.mobile.ui.theme.TransportHubTheme

@Composable
fun PreferenceScreen(navController: NavHostController)
{
    val padding = 2.dp
    TransportHubTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {
                PreferenceScreenTitle(navController)
                Spacer(Modifier.size(padding))
                //Divider(color = Color.LightGray, thickness = 1.dp)
                //ShowWifiSetting()
                //Spacer(Modifier.size(padding))
                //Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(Modifier.size(padding))
                ShowAboutGokigen()
                Spacer(Modifier.size(padding))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(Modifier.size(padding))
                ShowGokigenPrivacyPolicy()
                Spacer(Modifier.size(padding))
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun PreferenceScreenTitle(navController: NavHostController)
{
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        modifier = Modifier.clickable(onClick = { navController.popBackStack() }),
        backgroundColor = Color(0xff3DDC84),
        contentColor = if (isSystemInDarkTheme()) { Color.Black } else { Color.White },
    )
}

/*
@Composable
fun ShowWifiSetting()
{
    val context = LocalContext.current
    val density = LocalDensity.current
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(painter = painterResource(id = R.drawable.baseline_wifi_24),
            contentDescription = "Wifi",
            modifier = Modifier.clickable( onClick = {
                context.startActivity(Intent(Intent(Settings.ACTION_WIFI_SETTINGS)))
            })
        )
        Text(
            text = stringResource(R.string.pref_wifi_settings),
            fontSize = with(density) { 18.dp.toSp() },
            modifier = Modifier.padding(all = 4.dp)
                .clickable( onClick = {
                    context.startActivity(Intent(Intent(Settings.ACTION_WIFI_SETTINGS)))
                })
        )
    }
}
*/
@Composable
fun ShowAboutGokigen()
{
    val uriHandler = LocalUriHandler.current
    val openUri = stringResource(R.string.pref_instruction_manual_url)
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = stringResource(R.string.pref_instruction_manual),
                color = MaterialTheme.colors.primaryVariant,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = openUri,
                color = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier.clickable(onClick = { uriHandler.openUri(openUri) }),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ShowGokigenPrivacyPolicy()
{
    val uriHandler = LocalUriHandler.current
    val openUri = stringResource(R.string.pref_privacy_policy_url)
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = stringResource(R.string.pref_privacy_policy),
                color = MaterialTheme.colors.primaryVariant,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = openUri,
                color = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier.clickable(onClick = { uriHandler.openUri(openUri) }),
                fontSize = 14.sp
            )
        }
    }
}

