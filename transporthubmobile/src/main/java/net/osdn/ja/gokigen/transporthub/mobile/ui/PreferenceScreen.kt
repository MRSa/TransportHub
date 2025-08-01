package net.osdn.ja.gokigen.transporthub.mobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
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
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                PreferenceScreenTitle(navController)
                Spacer(Modifier.size(padding))
                Spacer(Modifier.size(padding))
                ShowAboutGokigen()
                Spacer(Modifier.size(padding))
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                Spacer(Modifier.size(padding))
                ShowGokigenPrivacyPolicy()
                Spacer(Modifier.size(padding))
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceScreenTitle(navController: NavHostController)
{
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        modifier = Modifier.clickable(onClick = { navController.popBackStack() }),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

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
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = openUri,
                color = MaterialTheme.colorScheme.primary,
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
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = openUri,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = { uriHandler.openUri(openUri) }),
                fontSize = 14.sp
            )
        }
    }
}
