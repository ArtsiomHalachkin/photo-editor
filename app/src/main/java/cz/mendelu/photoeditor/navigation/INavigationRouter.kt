package cz.mendelu.photoeditor.navigation

interface INavigationRouter {
    fun navigateToHomeScreen()
    fun navigateToStorageScreen()
    fun navigateToPreviewScreen(id: Long)
    fun navigateToEditorScreen(id: Long)
    fun navigateToCropScreen(id: Long)

    fun navigateToSettingsScreen()

    fun returnBack()

}