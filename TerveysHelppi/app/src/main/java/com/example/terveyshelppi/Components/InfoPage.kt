package com.example.terveyshelppi.Components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.ui.theme.*
import java.util.Stack

@Composable
fun InfoLanding(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var steps by remember { mutableStateOf(0) }
    var cal by remember { mutableStateOf(0) }
    var hours by remember { mutableStateOf(0) }

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
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                stringResource(id = R.string.ready),
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = semibold,
                modifier = Modifier.padding(top = 30.dp, start = 30.dp)
            )
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.name)) },
                modifier = Modifier
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .border(
                        BorderStroke(
                            2.dp, brush = Brush.verticalGradient(
                                colors = listOf(
                                    main1, main2
                                )
                            )
                        ), shape = RoundedCornerShape(10)
                    )
                    .background(Color.White, shape = RoundedCornerShape(10))
            )
            TextField(
                value = weight.toString(),
                onValueChange = { weight = it.toInt() },
                label = { Text(stringResource(id = R.string.weight)) },
                modifier = Modifier
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .border(
                        BorderStroke(
                            2.dp, brush = Brush.verticalGradient(
                                colors = listOf(
                                    main1, main2
                                )
                            )
                        ), shape = RoundedCornerShape(10)
                    )
                    .background(Color.White, shape = RoundedCornerShape(10))
            )
            Text(
                stringResource(id = R.string.goal),
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = semibold,
                modifier = Modifier.padding(top = 50.dp, start = 30.dp)
            )
            TextField(
                value = height.toString(),
                onValueChange = { height = it.toInt() },
                label = { Text(stringResource(id = R.string.height)) },
                modifier = Modifier
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .border(
                        BorderStroke(
                            2.dp, brush = Brush.verticalGradient(
                                colors = listOf(
                                    main1, main2
                                )
                            )
                        ), shape = RoundedCornerShape(10)
                    )
                    .background(Color.White, shape = RoundedCornerShape(10))
            )
            TextField(
                value = steps.toString(),
                onValueChange = { steps = it.toInt() },
                label = { Text(stringResource(id = R.string.step)) },
                modifier = Modifier
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .border(
                        BorderStroke(
                            2.dp, brush = Brush.verticalGradient(
                                colors = listOf(
                                    main1, main2
                                )
                            )
                        ), shape = RoundedCornerShape(10)
                    )
                    .background(Color.White, shape = RoundedCornerShape(10))
            )
            TextField(
                value = cal.toString(),
                onValueChange = { cal = it.toInt() },
                label = { Text(stringResource(id = R.string.cal)) },
                modifier = Modifier
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .border(
                        BorderStroke(
                            2.dp, brush = Brush.verticalGradient(
                                colors = listOf(
                                    main1, main2
                                )
                            )
                        ), shape = RoundedCornerShape(10)
                    )
                    .background(Color.White, shape = RoundedCornerShape(10))
            )
            TextField(
                value = hours.toString(),
                onValueChange = { hours = it.toInt() },
                label = { Text(stringResource(id = R.string.hour)) },
                modifier = Modifier
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                    .fillMaxWidth()
                    .border(
                        BorderStroke(
                            2.dp, brush = Brush.verticalGradient(
                                colors = listOf(
                                    main1, main2
                                )
                            )
                        ), shape = RoundedCornerShape(10)
                    )
                    .background(Color.White, shape = RoundedCornerShape(10))
            )
            Image(
                painterResource(id = R.drawable.next),
                "",
                modifier = Modifier
                    .padding(top = 50.dp, end = 30.dp)
                    .size(50.dp)
                    .clickable(onClick = {
                        navController.navigate("main")
                    })
                    .align(Alignment.End)

            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    TerveysHelppiTheme {
        val navController = rememberNavController()
        InfoLanding(navController)
    }
}