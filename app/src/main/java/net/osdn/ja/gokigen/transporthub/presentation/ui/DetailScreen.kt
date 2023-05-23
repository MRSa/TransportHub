package net.osdn.ja.gokigen.transporthub.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.osdn.ja.gokigen.transporthub.R
import net.osdn.ja.gokigen.transporthub.presentation.theme.GokigenComposeAppsTheme

@Composable
fun DataDetail(navController: NavHostController)
{
    GokigenComposeAppsTheme {
        Row {
            DetailScreenTitle(navController)
/*
            Text(text = stringResource(id = R.string.app_name),
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 32.dp, top = 32.dp , bottom = 0.dp, end = 32.dp)
                    .fillMaxSize()
                )
*/
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
                fontSize = 12.sp,
            )
    }
}
