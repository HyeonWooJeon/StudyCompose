package com.example.project2

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.project2.ui.theme.Project2Theme

data class Message(val text: String, val isReceived: Boolean)  // 데이터 모델 정의

class AlertPage : ComponentActivity() {

    private lateinit var messageReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project2Theme {
                var alertPermissionState by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val alertPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        alertPermissionState = isGranted
                        if(!isGranted){
                            Toast.makeText(context, "설정에서 알람 권한을 승인해주세요",Toast.LENGTH_LONG).show()
                        }
                    }
                )

                LaunchedEffect(Unit){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                        alertPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        alertPermissionState = true
                    }
                }


                val messages = remember { mutableStateListOf<Message>() }

                // 브로드캐스트 리시버 제작 및 등록
                messageReceiver = object : BroadcastReceiver(){
                    override fun onReceive(context: Context?, intent: Intent?) {
                        CHLog.d("log","Receive Success")
                        var intentMes =  intent?.getStringExtra("message")
                        messages.add(Message("$intentMes", isReceived = true))
                    }
                }

                val filter = IntentFilter()
                filter.addAction("new Message")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    registerReceiver(messageReceiver, filter, RECEIVER_NOT_EXPORTED)
                }else{
                    registerReceiver(messageReceiver, filter)
                }
                AlertView(messages = messages, onSendMessage = { text ->
                    messages.add(Message(text, isReceived = false))
                    // 여기에 서버로 메시지를 전송하는 로직 추가 가능
                })
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(messageReceiver)
    }
}

@Composable
fun AlertView(messages: List<Message>, onSendMessage: (String) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.bl_10))
    ) {
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            Image(
                painter = painterResource(id = R.drawable.bg_moj_symbol),
                contentDescription = "image"
            )
        }
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "채팅",
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Row {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.wt_100))
                ) {
                    Text(text = "전체", fontSize = 12.sp, color = colorResource(id = R.color.cg_60))
                }
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.wt_100))
                ) {
                    Text(text = "예약안내", fontSize = 12.sp, color = colorResource(id = R.color.cg_60))
                }
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.wt_100))
                ) {
                    Text(text = "공지사항", fontSize = 12.sp, color = colorResource(id = R.color.cg_60))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp)
                    .background(
                        color = colorResource(id = R.color.bl_20),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(messages) { message ->
                        MessageRow(message)
                    }
                }
                MessageInput(onSendMessage = onSendMessage)
            }
        }
    }
}

@Composable
fun MessageRow(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isReceived) Arrangement.Start else Arrangement.End
    ) {
        Text(
            text = message.text,
            modifier = Modifier
                .background(
                    color = if (message.isReceived) colorResource(id = R.color.bl_30) else colorResource(
                        id = R.color.cg_70
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            color = Color.White
        )
    }
}

@Composable
fun MessageInput(onSendMessage: (String) -> Unit) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        BasicTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(color = colorResource(id = R.color.wt_100))
                ) {
                    if (message.isEmpty()) {
                        Text(
                            text = "메시지 입력",
                            style = TextStyle(color = Color.Gray)
                        )
                    }
                    innerTextField()
                }
            }
        )
        Button(
            onClick = {
                if (message.isNotEmpty()) {
                    onSendMessage(message)
                    message = ""
                }
            },
            modifier = Modifier
                .padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.bl_100)),

        ) {
            Text(text = "전송")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlertPageShow() {
    Project2Theme {
        val messages = listOf(
            Message("", true),
            Message("", false)
        )
        AlertView(messages = messages, onSendMessage = {})
    }
}
