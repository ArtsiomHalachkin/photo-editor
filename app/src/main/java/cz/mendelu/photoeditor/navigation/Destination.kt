package cz.mendelu.photoeditor.navigation

sealed class Destination(val route: String) {
    object HomeScreen : Destination(route = "home_screen")
    object StorageScreen : Destination(route = "storage_screen")
    object PreviewScreen : Destination(route = "preview_screen")
    object EditorScreen : Destination(route = "editor_screen")
    object SettingsScreen : Destination(route = "settings_screen")
    object CropScreen : Destination(route = "crop_screen")

}