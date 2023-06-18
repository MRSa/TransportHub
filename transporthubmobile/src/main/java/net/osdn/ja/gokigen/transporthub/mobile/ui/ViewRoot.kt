package net.osdn.ja.gokigen.transporthub.mobile.ui


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.osdn.ja.gokigen.transporthub.mobile.model.DataListModel
import net.osdn.ja.gokigen.transporthub.mobile.ui.theme.TransportHubTheme

class ViewRoot @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AbstractComposeView(context, attrs, defStyleAttr)
{
    private val viewModel = DataListModel()

    @Composable
    override fun Content()
    {
        val navController = rememberNavController()

        TransportHubTheme {
            Surface {
                NavigationMain(context, navController, viewModel)
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
fun NavigationMain(context: Context, navController: NavHostController, dataListModel: DataListModel)
{
    TransportHubTheme {
        NavHost(navController = navController, startDestination = "MainScreen") {
            composable("MainScreen") {
                MobileApp(navController = navController, dataListModel = dataListModel)
            }
            composable(
                route = "DetailScreen/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                DataDetail(context = context, navController = navController, id = id)
            }
            composable("PreferenceScreen") {
                PreferenceScreen(navController = navController)
            }
        }
    }
}
