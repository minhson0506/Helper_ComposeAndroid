package com.example.terveyshelppi.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.terveyshelppi.R
import com.example.terveyshelppi.service.ResultViewModel
import com.example.terveyshelppi.service.youTubeService.YoutubeScreen
import com.example.terveyshelppi.ui.theme.regular

@ExperimentalFoundationApi
@Composable
fun FitnessPage(model: ResultViewModel, activity: AppCompatActivity) {
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
                    stringResource(id = R.string.fitness),
                    color = Color.White,
                    fontFamily = regular)
            },
            backgroundColor = Color.Black)
        Column(modifier = Modifier.padding(top = 50.dp)) {
            // display Youtube related screen
            YoutubeScreen(model = model, activity = activity)
        }
    }
}