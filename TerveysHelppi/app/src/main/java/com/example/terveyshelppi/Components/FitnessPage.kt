package com.example.terveyshelppi.Components

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.terveyshelppi.R
import com.example.terveyshelppi.Service.GetLocation
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.Service.YouTubeService.YoutubeScreen
import com.example.terveyshelppi.ui.theme.regular

@ExperimentalFoundationApi
@Composable
fun FitnessPage(model: ResultViewModel, activity: AppCompatActivity) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color(0xFF321A54)
                    )
                )
            )
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    stringResource(id = R.string.fitness), color = Color.White, fontFamily = regular)
            }, backgroundColor = Color.Black)
        Column(modifier = Modifier.padding(top = 50.dp)) {
//            Text(
//                stringResource(id = R.string.fitness),
//                color = Color.White,
//                modifier = Modifier.padding(top = 30.dp, start = 20.dp),
//                fontFamily = regular,
//                fontSize = 20.sp
//            )
            YoutubeScreen(model = model, activity)

        }
    }
}