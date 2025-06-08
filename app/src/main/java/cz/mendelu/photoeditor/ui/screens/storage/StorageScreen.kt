package cz.mendelu.photoeditor.ui.screens.storage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.photoeditor.database.Photo
import cz.mendelu.photoeditor.ui.elements.BaseScreen
import cz.mendelu.photoeditor.ui.elements.HomeBottomBar
import cz.mendelu.photoeditor.ui.theme.smallMargin
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import cz.mendelu.photoeditor.R
import cz.mendelu.photoeditor.navigation.INavigationRouter
import cz.mendelu.photoeditor.ui.elements.CustomDataPicker
import cz.mendelu.photoeditor.ui.elements.InfoElement
import cz.mendelu.photoeditor.ui.theme.halfMargin
import cz.mendelu.photoeditor.ui.theme.lightTextColor
import cz.mendelu.photoeditor.ui.theme.secondary
import cz.mendelu.photoeditor.ui.theme.tertiary
import cz.mendelu.photoeditor.ui.theme.textColor
import cz.mendelu.photoeditor.utils.DateUtils
import cz.mendelu.photoeditor.utils.DateUtils.Companion.getDateString

@Composable
fun StorageScreen(
    navigation: INavigationRouter
){


    val viewModel = hiltViewModel<StorageScreenViewModel>()
    val state = viewModel.storageUIState.collectAsStateWithLifecycle()



    LaunchedEffect(Unit) {
        viewModel.loadPhotos()
        viewModel.applyFilter()
    }


    BaseScreen(
        topBarText = stringResource(R.string.Storage),
        bottomBar = { HomeBottomBar(
            onHomeClick = {navigation.navigateToHomeScreen()},
            onGalleryClick = {navigation.navigateToStorageScreen()})}
    ) {
        StorageScreenContent(
            paddingValues = it,
            photos = state.value.photos,
            navigation = navigation,
            actions = viewModel,
            data = state.value
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageScreenContent(
    paddingValues: PaddingValues,
    photos: List<Photo>,
    navigation: INavigationRouter,
    actions: StorageScreenActions,
    data: StorageScreenUIState
){

    var showSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    if (showSheet) {

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {showSheet = false}
        ) {

            Box(
                modifier = Modifier
                    .background(color = tertiary)
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(smallMargin))

                    OutlinedTextField(
                        value = data.isoFilter,
                        onValueChange = { actions.onFilterIsoChange(it) },
                        label = { Text(stringResource(R.string.iso)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = lightTextColor,
                            focusedContainerColor = lightTextColor,
                            unfocusedLabelColor = lightTextColor,
                        )
                    )


                    if (showDatePicker) {
                        CustomDataPicker(
                            date = data.dateFilter,
                            onDateSelected = { actions.onFilterDateChange(it) },
                            onDismiss = { showDatePicker = false }
                        )
                    }

                    Spacer(modifier = Modifier.height(halfMargin))

                    InfoElement(
                        value = if (data.dateFilter != null) DateUtils.getDateString(data.dateFilter!!) else null,
                        hint = stringResource(R.string.date),
                        leadingIcon = Icons.Default.DateRange,
                        onClick = {
                            showDatePicker = true
                        }, onClearClick = {
                            actions.onFilterDateChange(null)
                        })

                    Spacer(modifier = Modifier.height(halfMargin))


                    Button(
                        onClick = {
                            actions.applyFilter()
                            showSheet = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = secondary,
                            contentColor = textColor()
                        )
                    ) {
                        Text(stringResource(R.string.Apply))
                    }
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (photos.isEmpty()) {
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.undraw_void_wez2),
                    contentDescription = stringResource(R.string.not_found),
                    modifier = Modifier.size(128.dp)
                )
            }

        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(smallMargin),
                verticalArrangement = Arrangement.spacedBy(smallMargin)
            ) {


                photos.forEach { photo ->
                    item {
                        PhotoItem(
                            photo = photo,
                            onRowClick = { navigation.navigateToPreviewScreen(photo.id!!) },
                            actions = actions
                        )
                    }
                }
            }




                Button(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = halfMargin, start = halfMargin),
                    onClick = {
                        photos.forEach {
                            if (it.photoState) {
                                actions.removePhotos(it)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = secondary,
                        contentColor = textColor()
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                    )

                }

            Button(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = halfMargin, end = halfMargin),
                onClick = {
                    showSheet = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = secondary,
                    contentColor = textColor()
                )
            ) {
                Text(stringResource(R.string.Filter))

            }
        }

    }

}

@Composable
fun PhotoItem(
    photo: Photo,
    onRowClick: () -> Unit,
    actions: StorageScreenActions
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = smallMargin,
                bottom = smallMargin
            )
            .clickable {onRowClick()}
            .height(160.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = photo.photoState,
            onCheckedChange = {
                actions.changePhotoState(photo.id!!, !photo.photoState)
            }
        )
        Column {
            Text(
                text = photo.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(smallMargin))

            if(photo.date != null) {
                Text(
                    text = getDateString(photo.date!!),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        AsyncImage(
            model = photo.url,
            contentDescription = photo.name,
            modifier = Modifier.size(160.dp),

        )
    }

}