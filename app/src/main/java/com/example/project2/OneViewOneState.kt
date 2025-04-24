package com.example.project2

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.map


class OneViewOneState : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel = hiltViewModel<OneStateViewModel>()
//            val state by mainViewModel.state.collectAsState()
            LaunchedEffect(key1 = Unit) {
                mainViewModel.processAction(MainAction.ContinueData)
            }
            Data(
//                state
            )
        }
    }
}

@Composable
fun Data(
//    state: MainState
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = androidx.compose.ui.Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize()
    ) {
        ContinueUpdateText(
//            state.text
        )
        PlayerA(
//            state.playerAText, state.listA
        )
        PlayerB(
//            state.playerBText, state.listB
        )
        PlayerC(
//            state.playerCText, state.listC
        )
    }
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun ContinueUpdateText(
//    value: String
) {
    val mainViewModel = hiltViewModel<OneStateViewModel>()
    val text by mainViewModel.state.map { it.text }.collectAsState(initial = "")
    Text(text = text, fontSize = 12.sp, color = Color.Black)
}
@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun PlayerA(
//    value: String, list: List<Int>
) {
    val mainViewModel = hiltViewModel<OneStateViewModel>()
    val playerAData by mainViewModel.state.map { it.playerAData }.collectAsState(initial = PlayerAData.initialData())
    LaunchedEffect(key1 = playerAData.text
//    value
    ) {
        CHLog.d("PlayerA", "Recomposed with value: ${playerAData.text}, ${playerAData.list}")
    }
    Text(text = playerAData.text, fontSize = 12.sp, color = Color.Magenta)
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun PlayerB(
//    value: String, list: List<Int>
) {
    val mainViewModel = hiltViewModel<OneStateViewModel>()
    val playerBData by mainViewModel.state.map { it.playerBData }.collectAsState(initial = PlayerBData.initialData())
    LaunchedEffect(key1 = playerBData.text) {
        CHLog.d("PlayerB", "Recomposed with value: ${playerBData.text}, ${playerBData.list}")
    }

    Text(text = playerBData.text, fontSize = 12.sp, color = Color.Magenta)
}
@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun PlayerC() {
    val mainViewModel = hiltViewModel<OneStateViewModel>()
    val playerCData by mainViewModel.state.map { it.playerCData }.collectAsState(initial = PlayerCData.initialData())
    LaunchedEffect(key1 = playerCData.text) {
        CHLog.d("PlayerC", "Recomposed with value: ${playerCData.text}, ${playerCData.list}")
    }

    Text(text = playerCData.text, fontSize = 12.sp, color = Color.Magenta)
}

