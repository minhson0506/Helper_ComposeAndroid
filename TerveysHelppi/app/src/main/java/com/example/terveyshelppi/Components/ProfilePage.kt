package com.example.terveyshelppi.Components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfilePage() {
    var mDisplayMenu by remember { mutableStateOf(false) }
    val mContext = LocalContext.current


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
            // Creating a Top bar
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.profile), color = Color.White, fontFamily = regular)
                }, backgroundColor = Color.Black,
                actions = {
                    // Creating Icon button for dropdown menu
                    Image(
                        painterResource(id = R.drawable.options),
                        "",
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(25.dp)
                            .clickable { mDisplayMenu = !mDisplayMenu }
                    )

                    // Creating a dropdown menu
                    DropdownMenu(
                        expanded = mDisplayMenu,
                        onDismissRequest = { mDisplayMenu = false }
                    ) {

                        // Creating dropdown menu item, on click
                        // would create a Toast message
                        DropdownMenuItem(onClick = {
                            Toast.makeText(
                                mContext,
                                "Edit profile",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                            Text(text = "Edit profile")
                        }

                        // Creating dropdown menu item, on click
                        // would create a Toast message
                        DropdownMenuItem(onClick = {
                            Toast.makeText(mContext, "Update goals", Toast.LENGTH_SHORT).show()
                        }) {
                            Text(text = "Update goals")
                        }
                    }
                }
            )
            /*Row(
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
            }*/

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
                    .padding(top = 10.dp, bottom = 10.dp),
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
                    .padding(start = 20.dp, end = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                val text = listOf(
                    "Most steps",
                    "Most floors",
                    "Duration",
                    "Calories burnt",
                    "Distance",
                    "Elevation"
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Text(
                        "Personal best", color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 10.dp, bottom = 10.dp),
                        fontFamily = semibold, fontSize = 14.sp
                    )
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(3)
                    ) {
                        items(text) {
                            Card(
                                modifier = Modifier.padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 10.dp
                                ),
                                backgroundColor = button
                            ) {
                                Column(horizontalAlignment = CenterHorizontally) {
                                    Image(
                                        painterResource(id = R.drawable.step),
                                        "",
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .size(20.dp)
                                    )
                                    Text(
                                        text = "20",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontFamily = semibold,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                    Text(
                                        text = it,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontFamily = regular,
                                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                                    )

                                }
                            }
                        }
                    }
                }

//                Column(modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 20.dp)
//                ) {
//                    Text(
//                        "Personal best", color = Color.White,
//                        modifier = Modifier.padding(top = 15.dp, start = 10.dp),
//                        fontFamily = semibold, fontSize = 14.sp
//                    )
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
//                    ) {
//                        Image(
//                            painterResource(id = R.drawable.step),
//                            "",
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Divider(modifier = Modifier.size(2.dp))
//                        Image(
//                            painterResource(id = R.drawable.cal),
//                            "",
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Image(
//                            painterResource(id = R.drawable.clock),
//                            "",
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
//                    ) {
//                        Text("1427", color = Color.White, fontSize = 15.sp, fontFamily = semibold)
//                        Text("200", color = Color.White, fontSize = 15.sp, fontFamily = semibold)
//                        Text("60", color = Color.White, fontSize = 15.sp, fontFamily = semibold)
//                    }
//                }
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