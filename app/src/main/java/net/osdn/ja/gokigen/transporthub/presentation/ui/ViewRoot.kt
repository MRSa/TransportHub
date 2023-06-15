package net.osdn.ja.gokigen.transporthub.presentation.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import net.osdn.ja.gokigen.transporthub.presentation.model.DataListModel
import net.osdn.ja.gokigen.transporthub.presentation.theme.GokigenComposeAppsTheme

class ViewRoot @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AbstractComposeView(context, attrs, defStyleAttr)
{
    private val viewModel = DataListModel()

    @Composable
    override fun Content()
    {
        val navController = rememberSwipeDismissableNavController()

        GokigenComposeAppsTheme {
            Surface(color = MaterialTheme.colors.background) {
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
    GokigenComposeAppsTheme {
        Scaffold()
        {
            SwipeDismissableNavHost(
            //NavHost(
                navController = navController,
                startDestination = "MainScreen",
            ) {
                composable(
                    route = "MainScreen"
                ) {
                    WearApp(navController = navController, dataListModel = dataListModel)
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
            }
        }
    }
}
