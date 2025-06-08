package cz.mendelu.photoeditor.ui.screens.editor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider

import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yalantis.ucrop.UCrop
import cz.mendelu.photoeditor.R
import cz.mendelu.photoeditor.navigation.INavigationRouter
import cz.mendelu.photoeditor.ui.elements.BaseScreen
import cz.mendelu.photoeditor.ui.elements.EditorBottomBar
import cz.mendelu.photoeditor.ui.theme.halfMargin
import cz.mendelu.photoeditor.ui.theme.primary
import cz.mendelu.photoeditor.ui.theme.secondary
import cz.mendelu.photoeditor.ui.theme.smallMargin
import cz.mendelu.photoeditor.ui.theme.tertiary
import cz.mendelu.photoeditor.ui.theme.textColor
import cz.mendelu.photoeditor.utils.ImagePickerUtils.Companion.loadBitmapFromUrl
import java.io.File
import java.io.FileOutputStream


@Composable
fun EditorScreen(
    navigation: INavigationRouter,
    id: Long
){

    val viewModel = hiltViewModel<EditorScreenViewModel>()
    val state = viewModel.editorUIState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(id) {
        viewModel.loadPhoto(id)

    }

    LaunchedEffect(state.value.newPhotoSaved) {
        if(state.value.newPhotoSaved){
            navigation.navigateToStorageScreen()
        }
    }

    LaunchedEffect(state.value.photoLoadedSuccesfuly) {
        if(state.value.photoLoadedSuccesfuly){
            val bitmap = loadBitmapFromUrl(context, state.value.photo.url)
            if (bitmap != null) {
                viewModel.setImage(bitmap)
            }else{
                Log.e("BITMAP", "BITMAP IS NULL ${state.value.photo.url}")
            }
        }
    }


   BaseScreen(
       topBarText = stringResource(R.string.Editor),
       onBackClick = {navigation.returnBack()},
       bottomBar = { EditorBottomBar(
           onCropClick = {navigation.navigateToCropScreen(id)},
           onGalleryClick = {navigation.navigateToStorageScreen()},
           onPreviewClick = {navigation.navigateToPreviewScreen(id)},
           onEditorClick = {navigation.navigateToEditorScreen(id)}
       )
       }
   ) {
       EditorScreenContent(
           paddingValues = it,
           data =state.value,
           actions = viewModel
       )
   }
}

@SuppressLint("RememberReturnType")
@Composable
fun EditorScreenContent(
    paddingValues: PaddingValues,
    data: EditorScreenUIState,
    actions: EditorScreenActions
){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            data.processedImage?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Image",
                    modifier = Modifier
                        .width(420.dp)
                        .height(420.dp),
                )
            } ?: Text(stringResource(R.string.not_found))

            Box(

            ) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = smallMargin
                        )
                ) {
                    Row(modifier = Modifier.
                    fillMaxWidth().
                    padding(all = smallMargin)
                    ) {
                        Text(stringResource(R.string.brightness))
                        Slider(
                            value = data.brightness,
                            onValueChange = { actions.updateBrightness(it) },
                            valueRange = -1f..2f,
                            modifier = Modifier.padding(smallMargin).height(smallMargin / 2),
                            colors = SliderDefaults.colors(
                                activeTrackColor = secondary,
                                inactiveTrackColor = primary

                            )
                        )
                    }
                    Row(modifier = Modifier.
                    fillMaxWidth().
                    padding(all = smallMargin))
                    {
                        Text(stringResource(R.string.contrast))
                        Slider(
                            value = data.contrast,
                            onValueChange = { actions.updateContrast(it) },
                            valueRange = 0.5f..2.5f,
                            modifier = Modifier.padding(smallMargin).height(smallMargin / 2),
                            colors = SliderDefaults.colors(
                                activeTrackColor = secondary,
                                inactiveTrackColor = tertiary

                            )
                        )
                    }
                    Row(modifier = Modifier.
                    fillMaxWidth().
                    padding(all = smallMargin))
                    {
                        Text(stringResource(R.string.saturation))
                        Slider(
                            value = data.saturation,
                            onValueChange = { actions.updateSaturation(it) },
                            valueRange = 0f..2f,
                            modifier = Modifier.padding(smallMargin).height(smallMargin / 2),
                            colors = SliderDefaults.colors(
                                activeTrackColor = secondary,
                                inactiveTrackColor = tertiary

                            )
                        )
                    }

                    Row(modifier = Modifier.
                    fillMaxWidth().
                    padding(all = smallMargin))
                    {
                        Text(stringResource(R.string.shadow))
                        Slider(
                            value = data.shadow,
                            onValueChange = { actions.updateShadow(it) },
                            valueRange = 0f..1f,
                            modifier = Modifier.padding(smallMargin).height(smallMargin / 2),
                            colors = SliderDefaults.colors(
                                activeTrackColor = secondary,
                                inactiveTrackColor = tertiary

                            )
                        )
                    }



                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = smallMargin),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {  actions.loadBack() },
                    modifier = Modifier.padding(bottom = smallMargin, start = halfMargin),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = secondary,
                        contentColor =  textColor()
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cancel),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))


                Button(
                    onClick = {  actions.savePhoto() },
                    modifier = Modifier.padding(bottom = smallMargin, end = halfMargin),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = secondary,
                        contentColor =  textColor()
                    )
                ) {
                    Text(stringResource(R.string.Apply))
                }
            }


        }

}