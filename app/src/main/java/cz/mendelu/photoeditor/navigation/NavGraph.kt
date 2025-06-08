package cz.mendelu.photoeditor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.photoeditor.ui.screens.cropper.CropScreen
import cz.mendelu.photoeditor.ui.screens.editor.EditorScreen
import cz.mendelu.photoeditor.ui.screens.home.HomeScreen
import cz.mendelu.photoeditor.ui.screens.preview.PreviewScreen
import cz.mendelu.photoeditor.ui.screens.settings.SettingsScreen
import cz.mendelu.photoeditor.ui.screens.storage.StorageScreen


@Composable
fun NavGraph(
    startDestination: String,
    navHostController: NavHostController = rememberNavController(),
    navRouter: INavigationRouter = remember {
        NavigationRouterImpl(navHostController)
    }
){
    NavHost(startDestination = startDestination, navController = navHostController) {

        composable(route = Destination.HomeScreen.route){
            HomeScreen(navRouter)
        }

        composable(route = Destination.StorageScreen.route){
            StorageScreen(navRouter)
        }

        composable(route = Destination.SettingsScreen.route){
            SettingsScreen(navRouter)
        }

        composable(route = "${Destination.EditorScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                }
            )
        ){
            val id = it.arguments!!.getLong("id")
            EditorScreen(navRouter, id)
        }

        composable(route = "${Destination.CropScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                }
            )
        ){
            val id = it.arguments!!.getLong("id")
            CropScreen(navRouter, id)
        }

        composable(route = "${Destination.PreviewScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                }
            )
        ){
            val id = it.arguments!!.getLong("id")
            PreviewScreen(navRouter, id)
        }

    }
}