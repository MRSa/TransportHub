package net.osdn.ja.gokigen.transporthub.presentation.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.AbstractComposeView
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.wear.compose.material.MaterialTheme
import net.osdn.ja.gokigen.transporthub.presentation.theme.GokigenComposeAppsTheme
import net.osdn.ja.gokigen.transporthub.storage.DataContent

class ViewRoot @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AbstractComposeView(context, attrs, defStyleAttr)
{
    private lateinit var dataList: SnapshotStateList<DataContent>

    fun setValues(dataList: SnapshotStateList<DataContent>)
    {
        this.dataList = dataList
        Log.v(TAG, " ...setValues...")
    }

    @Composable
    override fun Content()
    {
        val navController = rememberNavController()

        GokigenComposeAppsTheme {
            Surface(color = MaterialTheme.colors.background) {
                NavigationMain(navController, dataList)
            }
        }
        Log.v(TAG, " ...NavigationRootComponent...")
    }

    companion object
    {
        private val TAG = ViewRoot::class.java.simpleName
    }
}

@Composable
fun NavigationMain(navController: NavHostController, dataList: SnapshotStateList<DataContent>)
{
    GokigenComposeAppsTheme {
        NavHost(navController = navController, startDestination = "MainScreen") {
            composable("MainScreen") { WearApp(navController = navController, dataList = dataList) }
            composable(
                route = "DetailScreen/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                DataDetail(navController = navController, id = id)
            }
        }
    }
}
