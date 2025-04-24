package com.example.project2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme

class SubActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inputText = intent.getStringExtra("key")?: ""
        setContent {
            Project2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    subContent()
                    Log.d("SubActivity", "inputText!: $inputText")
                }
            }
        }
    }
}

@Composable
fun checkBox(
    checkedImage: Painter,
    uncheckedImage: Painter,
    initialCheck: Boolean = false,
    modifier: Modifier = Modifier
    ){
        var isChecked by remember { mutableStateOf(initialCheck) }

        Image(
            painter = if(isChecked) checkedImage else uncheckedImage,
            contentDescription = null,
            modifier = modifier
                .size(40.dp)
                .clickable { isChecked = !isChecked }
        )
    }

@Composable
fun subContent(){
    val context = LocalContext.current
    fun GoBackMain() {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    val unClickImage = painterResource(id = R.drawable.btn_uncheckbox)
    val clickImage = painterResource(id = R.drawable.btn_checkbox)
    val scrollState = rememberScrollState()
    val crG10 = colorResource(id = R.color.gg_10)

    Box(
        modifier= Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.tq_10))
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.tq_100),
                        colorResource(id = R.color.tq_10)
                    ),
                    startY = 0f,
                    endY = 270f
                ),            ),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .size(width = 360.dp, height = 1274.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
            , verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {GoBackMain()}) {
                Icon(painter = painterResource(id = R.drawable.icn_arrow),
                    contentDescription = "Dropdown Arrow",
                    tint = colorResource(R.color.wt_100),
                                 modifier = Modifier
                        .size(40.dp)
                        .rotate(90f)
                )
            }
            Text(
                text = "미디어 전송 정액제 가입&가입정보",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.wt_100),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .weight(1f)
                // 텍스트 시작 여백
            )
            Spacer(modifier = Modifier.width(40.dp))
        }

        Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                shape = RoundedCornerShape(10.dp),
                color = colorResource(id = R.color.wt_100)
            )
            .weight(1f)
            .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
            ){
            Text(
                text = "미디어 전송을 무제한으로 이용해보세요.",
                modifier = Modifier
                    .padding(start=15.dp, top = 40.dp, end = 15.dp),
                style = TextStyle(fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.gg_80),
                    fontStyle = FontStyle.Normal
                ),
            )
            Text(
                text = "미디어 전송 정액제에 가입하시면 횟수 제한 없이\n미디어 파일을 전송 할수 있어요",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 12.dp),
                style = TextStyle(fontSize = 12.sp),
                color = colorResource(id = R.color.gg_80)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 36.dp),
            ) {
                checkBox(
                    checkedImage = clickImage,
                    uncheckedImage = unClickImage
                )
                Text(text = "미디어 전송 정액제 가입 및 이용안내에\n동의합니다.",
               modifier = Modifier
                   .width(240.dp)
                   .weight(1f),
                    style = TextStyle(fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.gg_80),
                     )
                )
                Image(
                    painter = painterResource(id = R.drawable.icn_arrow),
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier
                        .size(40.dp)
                        .rotate(-90f)
                )
            }
          Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(start = 20.dp, end = 20.dp, top = 11.dp)
                    .drawWithContent {
                        drawContent() // 기존 컨텐츠를 그립니다.
                        clipRect { // 영역을 제한하여 그리기 작업을 수행합니다.
                            val strokeWidth = Stroke.DefaultMiter // 선의 기본 두께를 설정합니다.
                            val y1 = size.height * 0f  // 첫 번째 선의 y 위치를 설정합니다.
                            val y2 = size.height * 1f  // 두 번째 선의 y 위치를 설정합니다.
                            val x = size.width // 선의 x 위치를 설정합니다.
                            val x1 = size.width * 0f // 첫 번째 선의 x 위치를 설정합니다.
                            val x2 = size.width * 1f // 두 번째 선의 x 위치를 설정합니다.
                            val y = size.height

                            // 첫 번째 선 그리기
                            drawLine(
                                brush = SolidColor(crG10), // 선의 색상을 설정합니다.
                                strokeWidth = strokeWidth, // 선의 두께를 설정합니다.
                                cap = StrokeCap.Square, // 선의 끝 모양을 설정합니다.
                                start = Offset(0f, y1), // 선의 시작점을 설정합니다.
                                end = Offset(x, y1) // 선의 끝점을 설정합니다.
                            )

                            // 두 번째 가로 선 그리기
                            drawLine(
                                brush = SolidColor(crG10), // 선의 색상을 설정합니다.
                                strokeWidth = strokeWidth, // 선의 두께를 설정합니다.
                                cap = StrokeCap.Square, // 선의 끝 모양을 설정합니다.
                                start = Offset(0f, y2), // 선의 시작점을 설정합니다.
                                end = Offset(x, y2) // 선의 끝점을 설정합니다.
                            )

                            drawLine(
                                brush = SolidColor(crG10), // 선의 색상을 설정합니다.
                                strokeWidth = strokeWidth, // 선의 두께를 설정합니다.
                                cap = StrokeCap.Square, // 선의 끝 모양을 설정합니다.
                                start = Offset(x2 * 0.333f, 0f), // 선의 시작점을 설정합니다.
                                end = Offset(x2 * 0.333f, y) // 선의 끝점을 설정합니다.
                            )

                            drawLine(
                                brush = SolidColor(crG10), // 선의 색상을 설정합니다.
                                strokeWidth = strokeWidth, // 선의 두께를 설정합니다.
                                cap = StrokeCap.Square, // 선의 끝 모양을 설정합니다.
                                start = Offset(x2 * 0.666f, 0f), // 선의 시작점을 설정합니다.
                                end = Offset(x2 * 0.666f, y * 0.666f) // 선의 끝점을 설정합니다.
                            )

                            drawLine(
                                brush = SolidColor(crG10), // 선의 색상을 설정합니다.
                                strokeWidth = strokeWidth, // 선의 두께를 설정합니다.
                                cap = StrokeCap.Square, // 선의 끝 모양을 설정합니다.
                                start = Offset(0.333f, y * 0.333f), // 선의 시작점을 설정합니다.
                                end = Offset(x * 1f, y * 0.333f) // 선의 끝점을 설정합니다.
                            )

                            drawLine(
                                brush = SolidColor(crG10), // 선의 색상을 설정합니다.
                                strokeWidth = strokeWidth, // 선의 두께를 설정합니다.
                                cap = StrokeCap.Square, // 선의 끝 모양을 설정합니다.
                                start = Offset(0.666f, y * 0.666f), // 선의 시작점을 설정합니다.
                                end = Offset(x * 1f, y * 0.666f) // 선의 끝점을 설정합니다.
                            )
                        }
                    },
              verticalArrangement = Arrangement.Center
          ) {
              Row(
                  modifier = Modifier
                      .fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center
              ) {
                  Box(modifier = Modifier
                      .weight(1f)
                      .align(Alignment.CenterVertically)
                  ,contentAlignment = Alignment.Center)
                  {
                      Text(text = "",
                      )
                  }
                  Box(modifier = Modifier
                      .weight(1f)
                      .align(Alignment.CenterVertically)
                      ,
                      contentAlignment = Alignment.Center) {
                      Text(text = "사진",
                          fontSize = 12.sp,
                          color =  colorResource(id = R.color.gg_70),
                          fontWeight = FontWeight.SemiBold,
                      )
                  }

                  Box(modifier = Modifier
                      .weight(1f)
                      .align(Alignment.CenterVertically)
                      ,
                      contentAlignment = Alignment.Center) {

                      Text(text = "동영상",
                          fontSize = 12.sp,
                          color =  colorResource(id = R.color.gg_70),
                          fontWeight = FontWeight.SemiBold
                      )
                  }

              }
              Spacer(modifier = Modifier.size(8.dp))
              Row(modifier = Modifier
                  ,
              ) {
                  Box(modifier = Modifier
                      .weight(1f)
                      .align(Alignment.CenterVertically)
                      ,
                      contentAlignment = Alignment.Center){
                      Text(text = "건당 요금",
                          fontSize = 12.sp,
                          color =  colorResource(id = R.color.gg_70),
                      fontWeight = FontWeight.SemiBold)
                  }
                  Box(modifier = Modifier
                      .weight(1f)
                      .align(Alignment.CenterVertically)
                      ,contentAlignment = Alignment.Center){
                      Text(text = "220원",
                          fontSize = 12.sp,
                          color =  colorResource(id = R.color.gg_70))
                  }
                  Box(modifier = Modifier
                      .weight(1f)
                      .align(Alignment.CenterVertically)
                      ,
                      contentAlignment = Alignment.Center){
                      Text(text = "330원",
                          fontSize = 12.sp,
                          color =  colorResource(id = R.color.gg_70))
                  }
              }
              Spacer(modifier = Modifier.size(8.dp))
              Row() {
                  Box(modifier = Modifier
                      .weight(0.333f)
                      .align(Alignment.CenterVertically)
//                      .border(1.dp, colorResource(id = R.color.gg_10))
                      ,
                      contentAlignment = Alignment.Center){
                      Text(text = "정액제 요금",
                          fontSize = 12.sp,
                          color =  colorResource(id = R.color.gg_70),
                          fontWeight = FontWeight.SemiBold)
                  }
                  Box(modifier = Modifier
                      .weight(0.6666f)
                      .align(Alignment.CenterVertically)
                      ,
                      contentAlignment = Alignment.Center){
                      Text(text = "월 22,000원",
                          fontSize = 12.sp,
                          color =  colorResource(id = R.color.gg_70))
                  }
              }
          }
            Text(text = "* VAT포함",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = 20.dp),
                textAlign = TextAlign.Right,
                style = TextStyle(
                    fontSize = 8.sp,
                color = colorResource(id = R.color.gg_70)
                )
            )
            
            Text(text = "미디어 전송 정액제 서비스는 부가서비스에 가입하신 고객만 이용할 수 있어요.",
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp),
            fontSize = 12.sp,
            lineHeight = 16.8.sp,
            letterSpacing = -0.24.sp,
            color = colorResource(id = R.color.gg_50))

            Text(text = "가입정보",
            modifier = Modifier.padding(top=32.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp)
                    .background(
                        shape = RoundedCornerShape(10.dp),
                        color = colorResource(id = R.color.gg_1)
                    )
                    .border(
                        1.dp,
                        colorResource(id = R.color.gg_10),
                        shape = RoundedCornerShape(10.dp),
                    )
            ) {
                textList(text = "이름", textSub = "김이름", Modifier.padding(top=4.dp))
                textList(text = "가입일", textSub = "2023-04-07", Modifier)
                textList(text = "종료일", textSub = "2023-04-30", Modifier)
                textList(text = "다음 결제일", textSub = "2023-04-25 (자동연장)", Modifier.padding(bottom = 10.dp))
            }
                Text(text = "미디어 전송 정액제 가입 해지 요청은 고객센터로 문의해주세요.\n아미고 고객센터 : 02-569-1414",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp, end = 20.dp, top = 12.dp
                    ),
                    lineHeight = 16.8.sp,
                    letterSpacing = -0.24.sp,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gg_50))
        }

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RoundedCornerShape(50.dp), // 버튼 모서리를 둥글게 설정
            border = BorderStroke(1.dp, color = colorResource(id = R.color.tq_100))
        ) {
            Text(text = "가입하기",
            color= colorResource(id = R.color.tq_100))
        }
    }
}

@Composable
fun textList(text:String,textSub:String,modifier: Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 6.dp)
    ) {
        Text(text = text,
            modifier = modifier
                .width(66.dp),
        style=TextStyle(
        color = colorResource(id = R.color.gg_50),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        ))
        Text(text = textSub,
            modifier=modifier.weight(1f),
            style=TextStyle(
                color = colorResource(id = R.color.gg_70),
                fontSize = 12.sp
            ))
    }
}

@Preview(showBackground = true)
@Composable
fun subPreview() {
    Project2Theme {
        subContent()
    }
}

