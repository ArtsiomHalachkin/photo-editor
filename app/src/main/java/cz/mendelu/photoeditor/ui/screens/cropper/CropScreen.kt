package cz.mendelu.photoeditor.ui.screens.cropper

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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.motionEventSpy

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
import cz.mendelu.photoeditor.ui.theme.basicMargin
import cz.mendelu.photoeditor.ui.theme.halfMargin
import cz.mendelu.photoeditor.ui.theme.secondary
import cz.mendelu.photoeditor.ui.theme.smallMargin
import cz.mendelu.photoeditor.ui.theme.textColor
import cz.mendelu.photoeditor.utils.ImagePickerUtils.Companion.loadBitmapFromUrl
import java.io.File
import java.io.FileOutputStream


@Composable
fun CropScreen(
    navigation: INavigationRouter,
    id: Long
){



    val viewModel = hiltViewModel<CropScreenViewModel>()
    val state = viewModel.cropUIState.collectAsStateWithLifecycle()
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
       topBarText = stringResource(R.string.Crop),
       onBackClick = {navigation.returnBack()},
       bottomBar = { EditorBottomBar(
           onCropClick = {navigation.navigateToCropScreen(id)},
           onGalleryClick = {navigation.navigateToStorageScreen()},
           onPreviewClick = {navigation.navigateToPreviewScreen(id)},
           onEditorClick = {navigation.navigateToEditorScreen(id)},
       )
       }
   ) {
       EditorScreenContent(
           paddingValues = it,
           data =state.value,
           actions = viewModel,
           context = context
       )
   }
}

@SuppressLint("RememberReturnType")
@Composable
fun EditorScreenContent(
    paddingValues: PaddingValues,
    data: CropScreenUIState,
    actions: CropScreenActions,
    context: Context
){

    val cropLauncher = rememberLauncherForActivityResult (ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val resultUri = UCrop.getOutput(data)
                resultUri?.let { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    actions.apply(bitmap)
                }
            }
        }

    }

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
                        .width(500.dp)
                        .height(500.dp),
                )
            } ?: Text(stringResource(R.string.not_found))


            Spacer(modifier = Modifier.weight(1f))

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

                Button(onClick = {
                    data.processedImage?.let { bitmap ->
                        val file = File(context.cacheDir, "to_crop.jpg")
                        val outputStream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.flush()
                        outputStream.close()
                        val sourceUri = Uri.fromFile(file)

                        val destinationUri = Uri.fromFile(File(context.cacheDir, "cropped.jpg"))

                        val cropIntent = UCrop.of(sourceUri, destinationUri)
                            .withAspectRatio(1f, 1f)
                            .withMaxResultSize(1080, 1080)
                            .getIntent(context)

                        cropLauncher.launch(cropIntent)
                    }

                },
                    modifier = Modifier.padding(bottom = smallMargin),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = secondary,
                        contentColor =  textColor()
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Crop,
                        contentDescription = stringResource(R.string.Crop),
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