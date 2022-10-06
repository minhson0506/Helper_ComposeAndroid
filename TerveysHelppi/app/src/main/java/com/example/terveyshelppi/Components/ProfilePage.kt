package com.example.terveyshelppi.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terveyshelppi.R
import com.example.terveyshelppi.ui.theme.*

@Composable
fun ProfilePage() {
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
        Column() {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    stringResource(id = R.string.profile),
                    color = Color.White,
                    modifier = Modifier.padding(start = 20.dp),
                    fontFamily = regular,
                    fontSize = 20.sp
                )
                Image(
                    painterResource(id = R.drawable.options),
                    "",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(25.dp)
                )
            }

            Image(
                painterResource(id = R.drawable.dog),
                contentDescription = "dog",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(CenterHorizontally)
                    .size(100.dp)
                    .clip(CircleShape)

            )
            Text(
                stringResource(id = R.string.username),
                color = Color.White,
                fontFamily = semibold,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 10.dp),
                fontSize = 16.sp
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(bottom = 20.dp)) {
                    Text(
                        stringResource(id = R.string.weekly), color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 10.dp),
                        fontFamily = semibold, fontSize = 14.sp
                    )
                    Text(
                        stringResource(id = R.string.date), color = smallText,
                        modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                        fontFamily = light, fontSize = 12.sp
                    )
                    Row(modifier = Modifier.padding(top = 20.dp)) {
                        Text(
                            stringResource(id = R.string.average), color = Color.White,
                            modifier = Modifier.padding(start = 10.dp),
                            fontFamily = regular, fontSize = 14.sp
                        )
                        Text(
                            stringResource(id = R.string.time), color = Color.White,
                            modifier = Modifier.padding(start = 140.dp),
                            fontFamily = semibold, fontSize = 14.sp
                        )
                        Text(
                            stringResource(id = R.string.min), color = Color.White,
                            modifier = Modifier.padding(start = 5.dp),
                            fontFamily = light, fontSize = 14.sp
                        )
                    }

                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                ) {
                    Text(
                        "Personal best", color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 10.dp),
                        fontFamily = semibold, fontSize = 14.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                    ) {
                        Image(
                            painterResource(id = R.drawable.step),
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                        Image(
                            painterResource(id = R.drawable.cal),
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                        Image(
                            painterResource(id = R.drawable.clock),
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .padding(top = 95.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
                    ) {
                        Text("100/600", color = Color.White, fontSize = 15.sp, fontFamily = semibold)
                        Text("52/250", color = Color.White, fontSize = 15.sp, fontFamily = semibold)
                        Text("15/120", color = Color.White, fontSize = 15.sp, fontFamily = semibold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    TerveysHelppiTheme {
        ProfilePage()
    }
}