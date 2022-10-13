package com.example.terveyshelppi.Components

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.Service.ResultViewModel
import com.example.terveyshelppi.ui.theme.*

@Composable
fun LandingPage(navController: NavController, application: Application) {
    val TAG = "terveyshelppi"

    var user by remember { mutableStateOf("") }

    val viewModel = ResultViewModel(application)
    val data = viewModel.getInfo().observeAsState()

    if (data.value != null) {
        user = data.value?.name.toString()
    }

    Log.d(TAG, "LandingPage: $user")

    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        background
                    )
                )
            )
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painterResource(id = R.drawable.appicon),
                "",
                modifier = Modifier
                    .padding(top = 70.dp, bottom = 20.dp)
                    .size(60.dp),
                alignment = Alignment.Center
            )
            Text(
                stringResource(id = R.string.app_name),
                fontSize = 40.sp,
                fontFamily = bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        val brush = Brush.horizontalGradient(
                            colors = listOf(
                                main1,
                                main2
                            )
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
            Spacer(modifier = Modifier.size(300.dp))
            Text(
                stringResource(id = R.string.begin),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = regular,
                modifier = Modifier.padding(20.dp)
            )
            Image(
                painterResource(id = R.drawable.next),
                "",
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(50.dp)
                    .clickable(onClick = {
                        if (user != "") navController.navigate("main") else navController.navigate("details")
                    }),
                alignment = Alignment.Center
            )
        }
    }
}
