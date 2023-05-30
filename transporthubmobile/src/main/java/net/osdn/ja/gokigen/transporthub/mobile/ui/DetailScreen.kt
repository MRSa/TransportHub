package net.osdn.ja.gokigen.transporthub.mobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.osdn.ja.gokigen.transporthub.mobile.R
import net.osdn.ja.gokigen.transporthub.mobile.model.DetailModel

import net.osdn.ja.gokigen.transporthub.mobile.ui.theme.TransportHubTheme
@Composable
fun DataDetail(navController: NavHostController, id : Int)
{
    val model = DetailModel(id)

    TransportHubTheme {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 2.dp)
        ) {
            DetailScreenTitle(navController, model.detailData.title)
            ButtonArea(navController, model)
            Text(
                text = model.detailData.value,
                //color = TransportHubTheme.,
                fontSize = 12.sp,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenTitle(navController: NavHostController, title: String)
{
    TopAppBar(
        title = { Text(text = title) },
        modifier = Modifier.clickable(onClick = { navController.navigate("MainScreen") })
    )
}

@Composable
fun ButtonArea(navController: NavHostController, model: DetailModel)
{
    val deleteDialog = remember { mutableStateOf(false) }
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
            onClick = {
                // Delete Confirmation
                deleteDialog.value = true
            },
            enabled = true
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.LightGray
            )
        }
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

    // データ削除の確認
    if (deleteDialog.value) {
        AlertDialog(
            onDismissRequest = {
                deleteDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteDialog.value = false
                        model.deleteContent()
                        navController.navigate("MainScreen")
                    }
                ) {
                    Text(stringResource(id = R.string.delete_ok_label))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deleteDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.delete_cancel_label))
                }
            },
            title = {
                Text(stringResource(id = R.string.delete_confirm_title))
            },
            text = {
                val message = stringResource(id = R.string.delete_confirm_message) + " \n " + model.detailData.title
                Text(message)
            },
        )
    }
}
