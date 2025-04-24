package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    signView()
                }
            }
        }
    }
}

@Composable
fun signView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.wt_100))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 35.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                , horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "접근권한 설정안내",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "아래 권한을 모두 허용해야 AI 영상통화 서비스를 정상적으로 이용이 가능합니다.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                    , color = colorResource(id = R.color.cg_70), fontSize = 16.sp,lineHeight = 22.sp)
                Spacer(modifier = Modifier.size(32.dp))


                Information3(
                    selectColor = colorResource(id = R.color.bl_60),
                    selectImage = painterResource(id = R.drawable.icn_alert),
                    contentText = "alert",
                    textKey = "알림",
                    textSub = "통화 예약 승인정보와 예약일을 미리 알려드릴수 있습니다."
                )

                    Spacer(modifier = Modifier.size(17.dp))

                Information3(
                    selectColor = colorResource(id = R.color.rd_60),
                    selectImage = painterResource(id = R.drawable.icn_camera),
                    contentText = "camera",
                    textKey = "카메라",
                    textSub = "비대면인증, 영상통화, 얼굴 인증 로그인 서비스를 이용할 수 있습니다."
                )

                    Spacer(modifier = Modifier.size(17.dp))

                Information3(
                    selectColor = colorResource(id = R.color.YL_100),
                    selectImage = painterResource(id = R.drawable.icn_mic),
                    contentText = "mic",
                    textKey = "마이크",
                    textSub = "음성/영상 통화서비스를 이용하기 위해 필요합니다."
                )


                Spacer(modifier = Modifier.size(200.dp))
                DotsIndicator2(selectedColor = colorResource(id = R.color.cg_50), unSelectedColor = colorResource(
                    id = R.color.cg_20
                ), selectedIndex = 1, totalDots = 2)
                Spacer(modifier = Modifier.size(32.dp))

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(bottom = 20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.bl_100)),
                ) {
                    Text(text = "확인",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,)
                }
            }
        }
    }
}

@Composable
fun DotsIndicator2(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        for (i in 0 until totalDots) {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .size(if (i == selectedIndex) 8.dp else 4.dp)
                    .background(
                        if (i == selectedIndex) selectedColor else unSelectedColor,
                        CircleShape
                    )
            )
        }
    }
}

//3가지 전달사항에 대한 함수
@Composable
fun Information3(selectColor:Color,selectImage:Painter,contentText:String,textKey:String,textSub:String){
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier
            .size(64.dp)
            .background(
                color = selectColor,
                shape = RoundedCornerShape(16.dp)
            )) {

            Image(painter = selectImage,
                contentDescription = contentText,
                modifier = Modifier.padding(22.dp))
        }
        Column(modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)) {
            Text(text = textKey,
                color = colorResource(id = R.color.cg_90),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium)
            Text(text = textSub,
                color = colorResource(id = R.color.cg_70),
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInActivityShow() {
    Project2Theme {
        signView()
    }
}