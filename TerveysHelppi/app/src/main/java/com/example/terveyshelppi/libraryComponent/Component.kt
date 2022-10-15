package com.example.terveyshelppi.libraryComponent

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terveyshelppi.ui.theme.*

// TopAppBar
@Composable
fun TopAppBarWithBackButton(navController: NavController, id: Int) {
    TopAppBar(
        title = {
            Text(
                stringResource(id = id),
                color = Color.White,
                fontFamily = regular
            )
        }, backgroundColor = Color.Black,
        navigationIcon = if (navController.previousBackStackEntry != null) {
            {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        } else {
            null
        }
    )
}

// TextView
@Composable
fun TextModifiedWithId(
    id: Int,
    size: Int = 14,
    color: Color = Color.White,
    font: FontFamily = regular,
) {
    Text(
        text = stringResource(id = id),
        color = color,
        fontFamily = font,
        fontSize = size.sp
    )
}

@Composable
fun TextModifiedWithString(string: String, size: Int = 14, font: FontFamily = semibold) {
    Text(
        text = string,
        color = Color.White,
        fontFamily = font,
        fontSize = size.sp
    )
}

@Composable
fun TextModifiedWithPaddingStart(
    string: String,
    size: Int = 16,
    font: FontFamily = semibold,
    paddingStart: Int = 5,
) {
    Text(
        text = string,
        color = Color.White,
        fontFamily = font,
        fontSize = size.sp,
        modifier = Modifier.padding(start = paddingStart.dp)
    )
}

@Composable
fun TextModifiedWithPadding(id: Int) {
    Text(
        stringResource(id = id),
        color = Color.White,
        fontSize = 18.sp,
        fontFamily = semibold,
        modifier = Modifier.padding(top = 30.dp, start = 30.dp)
    )
}

@Composable
fun TextModifiedStringWithPadding(string: String) {
    Text(
        text = string,
        color = smallText,
        fontSize = 14.sp,
        fontFamily = regular,
        modifier = Modifier.padding(top = 5.dp, start = 30.dp)
    )
}
