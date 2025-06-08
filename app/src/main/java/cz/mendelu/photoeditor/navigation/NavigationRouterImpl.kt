package cz.mendelu.photoeditor.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {



    override fun navigateToHomeScreen() {
        navController.navigate(Destination.HomeScreen.route)
    }

    override fun navigateToStorageScreen() {
        navController.navigate(Destination.StorageScreen.route)
    }

    override fun navigateToPreviewScreen(id: Long) {
       navController.navigate(Destination.PreviewScreen.route + "/" + id)
    }

    override fun navigateToEditorScreen(id: Long) {
        navController.navigate(Destination.EditorScreen.route + "/" + id)
    }

    override fun navigateToCropScreen(id: Long) {
        navController.navigate(Destination.CropScreen.route + "/" + id)
    }

    override fun navigateToSettingsScreen() {
        navController.navigate(Destination.SettingsScreen.route)
    }


    override fun returnBack() {
        navController.popBackStack()
    }
}