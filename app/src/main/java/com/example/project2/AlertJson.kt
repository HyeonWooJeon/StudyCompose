package com.example.project2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.project2.ui.theme.Project2Theme
import com.google.firebase.messaging.FirebaseMessaging

data class alertMessage(val text: String, val isReceived: Boolean ,val type: String)


class AlertJson : ComponentActivity() {
    //Broadcast방식으로 받아오기
    private lateinit var messageReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //FCM 토큰 Log
//        FirebaseMessaging.getInstance().token.addOnCompleteListener {
//            it.result
//            CHLog.d("token",it.result)
//        }

        setContent {
            Project2Theme {
                var allButtonState by remember { mutableStateOf(true) }
                var noticeButtonState by remember { mutableStateOf(false) }
                var infoButtonState by remember { mutableStateOf(false) }
                val alertMessages = remember { mutableStateListOf<alertMessage>() }
                val filteredMessages = remember { mutableStateListOf<alertMessage>() }

                // 브로드캐스트 리시버 제작 및 등록
                messageReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        val intentDetail = intent?.getStringExtra("detail")
                        val intentType = intent?.getStringExtra("type")
                        if (intentDetail != null && intentType != null) {
                            //index를 이용해 신규 메세지를 최상단 배치
                            alertMessages.add(index = 0,alertMessage(intentDetail, isReceived = true, type = intentType))

                            updateFilteredMessages(
                                alertMessages,
                                allButtonState,
                                noticeButtonState,
                                infoButtonState,
                                filteredMessages
                            )
                        }
                    }
                }

                LaunchedEffect(allButtonState, infoButtonState, noticeButtonState) {
                    filteredMessages.clear()
                    filteredMessages.addAll(
                        when {
                            allButtonState -> alertMessages
                            infoButtonState -> alertMessages.filter { it.type == "info" }
                            noticeButtonState -> alertMessages.filter { it.type == "noti" }
                            //else로 예외 처리
                            else -> emptyList()
                        }
                    )
                }
                //한번만 반복되도록 launcheEffect로 지정
                LaunchedEffect(Unit) {
                    val filter = IntentFilter()
                    filter.addAction("newMessage")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        registerReceiver(messageReceiver, filter, RECEIVER_NOT_EXPORTED)
                    } else {
                        registerReceiver(messageReceiver, filter)
                    }
                }

                AlertJsonView(
                    alertMessages = filteredMessages,
                    allButtonState = allButtonState,
                    infoButtonState = infoButtonState,
                    noticeButtonState = noticeButtonState,
                    onAllButtonClick = {
                        allButtonState = true
                        noticeButtonState = false
                        infoButtonState = false
                        updateFilteredMessages(
                            alertMessages,
                            allButtonState,
                            noticeButtonState,
                            infoButtonState,
                            filteredMessages
                        )
                    },
                    onInfoButtonClick = {
                        allButtonState = false
                        noticeButtonState = true
                        infoButtonState = false
                        updateFilteredMessages(
                            alertMessages,
                            allButtonState,
                            noticeButtonState,
                            infoButtonState,
                            filteredMessages
                        )
                    },
                    onNoticeButtonClick = {
                        allButtonState = false
                        noticeButtonState = false
                        infoButtonState = true
                        updateFilteredMessages(
                            alertMessages,
                            allButtonState,
                            noticeButtonState,
                            infoButtonState,
                            filteredMessages
                        )
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(messageReceiver)
    }
}

fun updateFilteredMessages(
    alertMessages: SnapshotStateList<alertMessage>,
    allButtonState: Boolean,
    noticeButtonState: Boolean,
    infoButtonState: Boolean,
    filteredMessages: SnapshotStateList<alertMessage>
) {
    filteredMessages.clear()
    filteredMessages.addAll(
        when {
            allButtonState -> alertMessages
            noticeButtonState -> alertMessages.filter { it.type == "noti" }
            infoButtonState -> alertMessages.filter { it.type == "info" }
            else -> emptyList()
        }
    )
}
@Composable
fun AlertJsonView(
    alertMessages: List<alertMessage>,
//    onSendMessage: (String) -> Unit,
    allButtonState: Boolean,
    infoButtonState: Boolean,
    noticeButtonState: Boolean,
    onAllButtonClick: () -> Unit,
    onInfoButtonClick: () -> Unit,
    onNoticeButtonClick: () -> Unit,
//    viewModel: AlertJsonModel
) {
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
                text = "알림",
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Row {
                Button(
                    onClick = { onAllButtonClick() },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(32.dp)
                        .width(52.dp),
                    colors = if (allButtonState) ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.cg_60))
                    else ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.wt_100)),
                    //contentPadding을 이용해 버튼 규격과 이름을 작성해 넣어도 글씨가 짤리지 않게 조정
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "전체", fontSize = 12.sp, color = colorResource(id = if (allButtonState) R.color.wt_100 else R.color.cg_60))
                }
                Button(
                    onClick = { onNoticeButtonClick() },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(32.dp)
                        .width(74.dp),
                    colors = if (infoButtonState) ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.cg_60))
                    else ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.wt_100)), contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "예약안내", fontSize = 12.sp, color = colorResource(id = if (infoButtonState) R.color.wt_100 else R.color.cg_60))
                }
                Button(
                    onClick = { onInfoButtonClick() },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(32.dp)
                        .width(74.dp),
                    colors = if (noticeButtonState) ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.cg_60))
                    else ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.wt_100)), contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "공지사항", fontSize = 12.sp, color = colorResource(id = if (noticeButtonState) R.color.wt_100 else R.color.cg_60))
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .background(
                        color = colorResource(id = R.color.bl_20),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                ) {
                    items(alertMessages) { message ->
                        AlertMessageRow(alertMessage = message)
                    }
                }
            }
        }
    }
}

//FCM에서 넘어온 데이터 출력
@Composable
fun AlertMessageRow(alertMessage: alertMessage) {
    var time = "시간"
    //들어온 데이터 값으로 공지사항 또는 예약안내로 이름변경
    val typeText = when (alertMessage.type) {
        "noti" -> "공지사항"
        "info" -> "예약안내"
        else -> "기타"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column() {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = typeText, fontSize = 12.sp, modifier = Modifier.padding(start = 3.dp), color = colorResource(
                    id = R.color.cg_70
                ))

                Text(text = time, fontSize = 12.sp, color = colorResource(id = R.color.cg_50))
            }

                Text(
                    text = alertMessage.text,
                    modifier = Modifier
                        .padding(start = 38.dp, top = 12.dp, bottom = 12.dp),
                    color = colorResource(id = R.color.cg_90), fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            Text(text = "알림을 눌러 입장하기 >", modifier = Modifier.padding(start = 38.dp), fontSize = 12.sp, color = colorResource(
                id = R.color.cg_70
            ))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlertJsonShow() {
    Project2Theme {
        val messages = listOf(
            alertMessage("수용자 영상통화 5분전 입니다.", true,"noti"),
        )
        AlertJsonView(
            alertMessages = messages,
//          onSendMessage = {},
            allButtonState = true,
            infoButtonState = false,
            noticeButtonState = false,
            onAllButtonClick = {},
            onInfoButtonClick = {},
            onNoticeButtonClick = {}
        )
    }
}
