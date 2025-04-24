package com.example.project2

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project2.ui.theme.Project2Theme
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Async :  androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstantState: Bundle?){
        super.onCreate(savedInstantState)
        setContent{
            Project2Theme {
                AsyncAwaitExample()
            }
        }
    }
}

@Composable
fun AsyncAwaitExample() {
    var result by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column() {
        Text(text = result)
        Button(onClick = {
            coroutineScope.launch {
                result ="데이터 부르는 중.."
                val data = fetchData()
                result = data
            }
        }) {
            Text(text= "데이터 부르기")
            }
        }
    }
}

suspend fun fetchData(): String = coroutineScope {
    val apiCall1 = async { networkRequest1() }
    val apiCall2 = async { networkRequest2() }

// 두 네트워크 요청을 병렬로 실행한 후, 결과를 결합
    val result1 = apiCall1.await()
    val result2 = apiCall2.await()

    "1번 반환: $result1, 2번 반환: $result2"
}
//delay 함수를 사용하려면 suspend를 사용해 일시정지 후 진행시킬수 있도록 해야함
//suspend fun은 코루틴 내부나 suspend fun 내부에서만 사용가능
suspend fun networkRequest1(): String {
    delay(2000) // 네트워크 요청을 시뮬레이션하기 위해 딜레이 추가
    return "데이터 1 번"
}

suspend fun networkRequest2(): String {
    delay(3000) // 네트워크 요청을 시뮬레이션하기 위해 딜레이 추가
    return "데이터 2 번"
}

@Preview(showBackground = true)
@Composable
fun asyncView() {
    Project2Theme {
        AsyncAwaitExample()
    }
}
