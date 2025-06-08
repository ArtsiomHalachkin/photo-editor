package cz.mendelu.photoeditor.ui.screens.preview

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import cz.mendelu.photoeditor.R
import cz.mendelu.photoeditor.navigation.INavigationRouter
import cz.mendelu.photoeditor.ui.elements.BaseScreen
import cz.mendelu.photoeditor.ui.elements.CustomDataPicker
import cz.mendelu.photoeditor.ui.elements.EditorBottomBar
import cz.mendelu.photoeditor.ui.elements.InfoElement
import cz.mendelu.photoeditor.ui.theme.halfMargin
import cz.mendelu.photoeditor.ui.theme.lightTextColor
import cz.mendelu.photoeditor.ui.theme.secondary
import cz.mendelu.photoeditor.ui.theme.smallMargin
import cz.mendelu.photoeditor.ui.theme.tertiary
import cz.mendelu.photoeditor.ui.theme.textColor
import cz.mendelu.photoeditor.utils.DateUtils


@Composable
fun PreviewScreen(
    navigation: INavigationRouter,
    id: Long,
){

    val viewModel = hiltViewModel<PreviewScreenViewModel>()
    val state = viewModel.previewUIState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadPhoto(id)
    }

    if (state.value.photoSaved){
        LaunchedEffect(state.value.photoSaved){
            navigation.returnBack()
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.Information),
        onBackClick = {navigation.returnBack()},
        bottomBar = { EditorBottomBar(
            onCropClick = {navigation.navigateToCropScreen(id)},
            onGalleryClick = {navigation.navigateToStorageScreen()},
            onPreviewClick = {navigation.navigateToPreviewScreen(id)},
            onEditorClick = {navigation.navigateToEditorScreen(id)}
        )}
    ) {
        PreviewScreenContent(
            paddingValues = it,
            data =state.value,
            navigation = navigation,
            actions = viewModel
        )
    }
}

@SuppressLint("Range", "ConfigurationScreenWidthHeight")
@Composable
fun PreviewScreenContent(
    paddingValues: PaddingValues,
    data: PreviewScreenUIState,
    navigation: INavigationRouter,
    actions: PreviewScreenActions
) {

    val scrollState = rememberScrollState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp



    var showDatePicker by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        AsyncImage(
            model = data.photo.url,
            contentDescription = data.photo.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(
                    top = smallMargin,
                    bottom = smallMargin,
                )
        )

        Box(
            modifier = Modifier
                .background(color = tertiary)
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = smallMargin,
                        bottom = smallMargin,
                        start = smallMargin,
                        end = halfMargin
                    )
                    .verticalScroll(scrollState)
            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = data.photo.name,
                    onValueChange = {
                        actions.onNameChanged(it)
                    },
                    label = {
                        Text(stringResource(R.string.name))
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = lightTextColor,
                        focusedContainerColor = lightTextColor,
                        focusedBorderColor = tertiary,
                        focusedLabelColor = lightTextColor,
                        unfocusedLabelColor = lightTextColor,

                        cursorColor = tertiary

                    )
                )


                Spacer(modifier = Modifier.height(halfMargin))



                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),

                    value = data.photo.description,
                    onValueChange = {
                        actions.onDescriptionChanged(it)
                    },
                    label = { Text(stringResource(R.string.description)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = lightTextColor,
                        focusedContainerColor = lightTextColor,
                        focusedBorderColor = tertiary,
                        unfocusedLabelColor = lightTextColor,
                        focusedLabelColor = lightTextColor,
                        cursorColor = tertiary


                    )
                )

                if (showDatePicker) {
                    CustomDataPicker(
                        date = data.photo.date,
                        onDateSelected = { actions.onDateChanged(it) },
                        onDismiss = { showDatePicker = false }
                    )
                }

                Spacer(modifier = Modifier.height(halfMargin))

                InfoElement(
                    value = if (data.photo.date != null) DateUtils.getDateString(data.photo.date!!) else null,
                    hint = stringResource(R.string.date),
                    leadingIcon = Icons.Default.DateRange,
                    onClick = {
                        showDatePicker = true
                    }, onClearClick = {
                        actions.onDateChanged(null)
                    })


                Spacer(modifier = Modifier.height(halfMargin))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = data.photo.iso,
                    onValueChange = {
                        actions.onIsoChanged(it)
                    },
                    label = {
                        Text("ISO")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = lightTextColor,
                        focusedContainerColor = lightTextColor,
                        focusedBorderColor = tertiary,
                        unfocusedLabelColor = lightTextColor,
                        focusedLabelColor = lightTextColor,
                        cursorColor = tertiary


                    )
                )

                Spacer(modifier = Modifier.height(halfMargin))

            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),

            onClick = {
                actions.savePhoto()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = secondary,
                contentColor = textColor()
            )
        )
        {
            Text(stringResource(R.string.save))
        }
    }

}