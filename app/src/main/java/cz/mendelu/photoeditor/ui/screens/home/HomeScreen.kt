package cz.mendelu.photoeditor.ui.screens.home

import android.Manifest
import android.content.Context

import android.content.pm.PackageManager
import android.net.Uri

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import cz.mendelu.photoeditor.R
import cz.mendelu.photoeditor.database.Photo
import cz.mendelu.photoeditor.navigation.INavigationRouter
import cz.mendelu.photoeditor.ui.elements.BaseScreen
import cz.mendelu.photoeditor.ui.elements.CameraGallerySheet
import cz.mendelu.photoeditor.ui.elements.HomeBottomBar
import cz.mendelu.photoeditor.ui.screens.storage.PhotoItem
import cz.mendelu.photoeditor.ui.screens.storage.StorageScreenActions
import cz.mendelu.photoeditor.ui.theme.background
import cz.mendelu.photoeditor.ui.theme.basicMargin
import cz.mendelu.photoeditor.ui.theme.halfMargin
import cz.mendelu.photoeditor.ui.theme.secondary
import cz.mendelu.photoeditor.ui.theme.smallMargin
import cz.mendelu.photoeditor.ui.theme.tertiary
import cz.mendelu.photoeditor.ui.theme.textColor
import cz.mendelu.photoeditor.utils.AppVersionUtils.Companion.getAppVersion
import cz.mendelu.photoeditor.utils.DateUtils.Companion.getDateString
import java.io.File



@Composable
fun HomeScreen(
    navigation: INavigationRouter
){

    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val state = viewModel.homeScreenUIState.collectAsStateWithLifecycle()
    val showSheet = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadRecentPhotos()
    }


    LaunchedEffect(state.value.photoPicked) {
        if (state.value.photoPicked){
            navigation.navigateToStorageScreen()
        }
    }


    //gallery picker
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use {
                val bytes = it.readBytes()
                viewModel.uploadPickedPhoto(bytes)
                //state.value.photoPicked = true
            }
        }
    }


    //camera picker
    val photoFile = File.createTempFile("camera_", null,context.cacheDir) //temporary file to save the photo taken by the camera
    val photoUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // defined in Manifest
        photoFile
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> // photo was taken
        if (success) {
            val inputStream = context.contentResolver.openInputStream(photoUri)
            inputStream?.use {
                val bytes = it.readBytes()
                viewModel.uploadPickedPhoto(bytes)
                //state.value.photoPicked = true
            }
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(photoUri)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }




    BaseScreen(
        topBarText = stringResource(R.string.app_name),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showSheet.value = true
            }, containerColor = secondary, contentColor = textColor()) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_photo))
            }
        },
        bottomBar = {
            HomeBottomBar(
                onHomeClick = { navigation.navigateToHomeScreen() },
                onGalleryClick = { navigation.navigateToStorageScreen() }
            )
        }
    ) {
        HomeScreenContent(
            paddingValues = it,
            actions = viewModel,
            navigation = navigation,
            context = context,
            data = state.value
        )
    }

    if (showSheet.value) {
        CameraGallerySheet(
            onCameraClick = {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    cameraLauncher.launch(photoUri)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onGalleryClick = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onDismiss = { showSheet.value = false }
        )
    }



}

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    actions: HomeScreenViewModel,
    navigation: INavigationRouter,
    context: Context,
    data: HomeScreenUIState
) {

    val version = remember { getAppVersion(context) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        if (data.photos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = halfMargin, end = halfMargin)
                    .statusBarsPadding()
                    .background(tertiary)
            ) {
                Row(
                    modifier = Modifier.padding(
                        top = smallMargin,
                        start = smallMargin,
                        bottom = smallMargin
                    ).fillMaxSize().height(128.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.undraw_void_wez2),
                        contentDescription = stringResource(R.string.not_found),
                        modifier = Modifier.size(128.dp)
                    )
                }
            }

        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = halfMargin, end = halfMargin)
                    .statusBarsPadding()
                    .background(tertiary)
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = smallMargin),
                    horizontalArrangement = Arrangement.spacedBy(smallMargin / 2)
                ) {
                    data.photos.forEach { photo ->
                        item {
                            PhotoItem(
                                photo = photo,
                                onRowClick = { navigation.navigateToEditorScreen(photo.id!!) }
                            )
                        }
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_screen_svg),
                contentDescription = stringResource(R.string.home),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.version) + " $version",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )

        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(bottom = smallMargin),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = {  navigation.navigateToSettingsScreen()},
                modifier = Modifier.padding(bottom = smallMargin, start = halfMargin),
                colors = ButtonDefaults.buttonColors(
                    containerColor = secondary,
                    contentColor =  textColor()
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.Settings),
                )
            }
        }
    }
}

@Composable
fun PhotoItem(
    photo: Photo,
    onRowClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = smallMargin,
                bottom = smallMargin
            )
            .clickable {onRowClick()}
            .height(128.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        AsyncImage(
            model = photo.url,
            contentDescription = photo.name,
            modifier = Modifier.size(128.dp),
            )
    }

}


