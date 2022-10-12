package com.example.terveyshelppi.Components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.terveyshelppi.Service.StopWatch
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.Service.drawLineInMap
import org.osmdroid.util.GeoPoint

@Composable
fun ExerciseResult(model: ResultViewModel) {

//    val listPoints by model.listPoints.observeAsState(null)
//
//    val long by model.long.observeAsState()
//    val lat by model.lat.observeAsState()

//    if (listPoints != null)
//        if (listPoints!!.size >= 2)
//            listPoints?.let {
//                lat?.let { it1 ->
//                    long?.let { it2 -> GeoPoint(it1, it2) }
//                }?.let { it2 -> drawLineInMap(points = it, currentPoint = it2) }
//            }
//        else Text("Nothing")
//    else Text("Null")
    drawLineInMap()

}