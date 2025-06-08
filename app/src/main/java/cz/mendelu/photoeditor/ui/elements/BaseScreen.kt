package cz.mendelu.photoeditor.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.mendelu.photoeditor.ui.theme.background
import cz.mendelu.photoeditor.ui.theme.primary
import cz.mendelu.photoeditor.ui.theme.secondary
import cz.mendelu.photoeditor.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String,
    onBackClick: (() -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = topBarText,color = textColor()) },
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                colors = topAppBarColors(
                    containerColor = primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        },
        floatingActionButton = { floatingActionButton() },
        bottomBar = {
            BottomAppBar(
                containerColor = secondary,
                contentColor = textColor()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ){
                bottomBar()
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(background)

        ) {
            content(padding)
        }
    }
}