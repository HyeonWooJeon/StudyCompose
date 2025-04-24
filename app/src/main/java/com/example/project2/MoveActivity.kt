package com.example.project2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class MoveActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val from = intent.getStringExtra("from")
        if(from !=null){
        startActivity(Intent(this, AlertPage::class.java))
        }

        setContent {
            Project2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    MyPagerScreen(coroutineScope)
                }
            }
        }

        //
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // FCM에서 새로운 토큰값 얻기
            val token = task.result

            // 토큰 토스트로 출력
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("TAG", msg)
            //toast 추후 제거
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("intentMessage","message")
        Log.d("intentMessage","${intent?.getStringExtra("from")}")
    }
}

// offset고정 스와이프시 화면은 고정되고 FadeOut되는 효과를 graphicsLayer로 지정
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerFadeTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset = calculateCurrentOffsetForPage(page, pagerState)
        translationX = pageOffset * size.width
        alpha = 1 - pageOffset.absoluteValue
    }
// 이미지가 아니므로 현재 View의 사이즈를 구하기
@OptIn(ExperimentalFoundationApi::class)
fun calculateCurrentOffsetForPage(page: Int, pagerState: PagerState): Float {
    return pagerState.currentPage - page + pagerState.currentPageOffsetFraction
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyPagerScreen(coroutineScope: CoroutineScope) {
    val context = LocalContext.current
    fun navigateToIdProject() {
        val intent = Intent(context, IdProject::class.java)
        context.startActivity(intent)
    }
    var showDialog by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState()
    val pageCount = 2

    //Back Hander 구성
    BackHandler(enabled = pagerState.currentPage !=0) {
        coroutineScope.launch {
            if(pagerState.currentPage>0){
                pagerState.animateScrollToPage(pagerState.currentPage -1)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.wt_100)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.moj_symbol_32), contentDescription = "법무부Logo", modifier = Modifier.padding(end=6.dp))
            Image(painter = painterResource(id = R.drawable.moj_logotype_56), contentDescription = "법무부Name")
        }
        //Pager지정
        HorizontalPager(
            pageCount = pageCount,
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            Box(modifier = Modifier
                .pagerFadeTransition(page, pagerState)
                .fillMaxWidth()) {
                when (page){
                    0-> moveView()
                    1-> signView()
                }
            }
        }

        //인디케이터의 옵션
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount =pageCount ,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .background(color = colorResource(id = R.color.wt_100)),
            activeColor = colorResource(id = R.color.cg_50),
            inactiveColor = colorResource(id = R.color.cg_20),
            indicatorShape = CircleShape,
            indicatorWidth = 8.dp,
            spacing = 8.dp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage == 0) {
                        pagerState.animateScrollToPage(1)
                    }else if(pagerState.currentPage == 1){
                        //alert 함수 함수내에서 ok시 다음 뷰
                        showDialog = true
                        }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.bl_100)),

        ) {
            Text(
                text = "다음",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        if(showDialog) {
            alertDialog(onDismiss = {showDialog=false},
                onConfirm = {
                    showDialog=false
                    coroutineScope.launch {
                        navigateToIdProject()
                    }
                }
            )
        }
    }
}

@Composable
fun alertDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("허용 안 함")
            }
        },
        text = {
            Text("'NAVER'이(가) 카메라에 접근하려고 합니다.\n이미지 검색 및 사진 첨부 기능을 이용할 수 있습니다.(필수권한)")
        }
    )
}


@Composable
fun moveView(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.wt_100))
    ) {
        Column() {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp, start = 20.dp, end = 20.dp)
            , horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "AI영상통화 서비스 이용안내",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "AI영상통화 서비스는 기존 인터넷 접견보다 접근성 있게 사용할 수 있도록 제공되는 서비스 입니다.",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(), fontSize = 16.sp
                , color = colorResource(id = R.color.cg_70))
            Spacer(modifier = Modifier.size(78.dp))
            Box(modifier = Modifier
                    .width(320.dp)
                    .height(320.dp)
                    .border(1.dp, color = colorResource(id = R.color.cg_20))
               ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, top = 7.dp)){
                                    //중앙원 점선
                                    imageDesign(startP = 72.dp,
                                        topP = 59.dp,
                                        endP = 76.dp,
                                        bottomP = 89.dp,
                                        painter = painterResource(id = R.drawable.circle__1_),
                                        contentKeyword = "inCircle")
                                    //중앙 밖원 점선
                                    imageDesign(startP = 23.dp,
                                        topP = 13.04.dp,
                                        endP = 37.dp,
                                        bottomP = 50.03.dp,
                                        painter = painterResource(id = R.drawable.circle__1_),
                                        contentKeyword = "outCircle")
                                    //Ai logo
                                    imageDesign(startP = 118.52.dp,
                                        topP = 108.56.dp,
                                        endP = 132.52.dp,
                                        bottomP = 145.52.dp,
                                        painter = painterResource(id = R.drawable.ai),
                                        contentKeyword = "AILogo")
                                    //pc와 공중전화 사이 원
                                    moveTransition(startValue = 6.dp,
                                        moveValue = 10.dp,
                                        image = painterResource(id = R.drawable.ball),
                                        offsetX = 230.dp,
                                        offsetY = 134.dp)
                                    //최상단 원
                                    moveTransition(image=painterResource(id = R.drawable.ball__1_),
                                        moveValue =  38.dp,
                                        startValue = 32.dp,
                                        offsetX =  114.dp ,
                                        offsetY =  0.dp )
                                    //공중전화 왼쪽 원
                                    moveTransition(startValue = 16.dp,
                                        moveValue = 20.dp,
                                        image = painterResource(id = R.drawable.ball__2_),
                                        offsetX = 140.dp,
                                        offsetY = 213.dp)
                                    //휴대폰 오른쪽 원
                                    moveTransition(startValue = 10.dp,
                                        moveValue = 14.dp,
                                        image = painterResource(id = R.drawable.ball__3_),
                                        offsetX = 90.dp,
                                        offsetY = 78.dp)
                                    moveTransition(startValue = 10.dp,
                                        moveValue = 8.dp,
                                        image = painterResource(id = R.drawable.ball__4_),
                                        offsetX = 43.dp,
                                        offsetY = 206.dp)

                                    // 컴퓨터 이미지 애니메이션
                                    imageDesign(startP = 208.dp, topP = 16.dp, endP = 10.dp,
                                        bottomP = 216.dp, painter = painterResource(id = R.drawable.pc), contentKeyword = "pc",
                                        sizeX = 112.dp, sizeY = 88.dp, startValueX = 10.dp,
                                        startValueY = -0.dp, moveValueX = 0.dp, moveValueY = -10.dp)

                                    // 휴대폰 이미지 애니메이션
                                    imageDesign(startP = 0.dp, topP = 62.04.dp, endP = 226.dp,
                                        bottomP = 151.dp, painter = painterResource(id = R.drawable.mobile), contentKeyword = "phone",
                                        sizeX = 84.dp, sizeY = 100.dp,
                                        startValueY = 0.dp,  moveValueY = 10.dp)

                                    // 공중전화 이미지 애니메이션
                                    imageDesign(startP = 158.41.dp, topP = 192.dp, endP = 39.59.dp,
                                        bottomP = 10.dp, painter = painterResource(id = R.drawable.pubtel), contentKeyword = "payPhone",
                                        sizeX = 112.dp, sizeY = 118.dp, startValueX = 0.dp, moveValueX = 12.dp,
                                        startValueY = 10.dp,  moveValueY = 0.dp)
                        }
            }
        }
            Spacer(modifier = Modifier.size(58.dp))
            Row(modifier = Modifier .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
                ){
            }
            Spacer(modifier = Modifier.size(32.dp))
        }
    }
}

//원의 움직임 처리하는 함수
@Composable
fun moveTransition(
    startValue: Dp,
    moveValue: Dp,
    image: Painter,
    offsetX: Dp,
    offsetY: Dp
) {
    val transition = rememberInfiniteTransition()
    // 애니메이션 옵션지정
    val newTransition by transition.animateValue(
        initialValue = startValue,
        targetValue = moveValue,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    // 이미지와 애니메이션화 된 offset 적용
    Image(
        painter = image,
        contentDescription = "Animated Image",
        modifier = Modifier
            .offset(
                offsetX,
                offsetY
            )
            .size(newTransition)
    )
}
//고정이미지 offset, 움직이는 이미지 배치 후 x값과 y값을 세팅후 애니매이션화 시킨 함수
@Composable
fun imageDesign(
    startP: Dp,
    topP: Dp,
    endP: Dp,
    bottomP: Dp,
    painter: Painter,
    contentKeyword: String,
    size: Dp? = null,
    sizeX: Dp? = null,
    sizeY: Dp? = null,
    startValueX: Dp? = null,
    moveValueX: Dp? = null ,
    startValueY: Dp? = null,
    moveValueY: Dp? = null
) {
    val transition = rememberInfiniteTransition()

    // 애니메이션 x축
    val newTransitionX by transition.animateValue(
        initialValue = startValueX ?: 0.dp,
        targetValue = moveValueX ?: 0.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    //애니메이션 y축
    val newTransitionY by transition.animateValue(
        initialValue = startValueY ?: 0.dp,
        targetValue = moveValueY ?: 0.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    //Modifier 변수로 받아 if문 구성
    var modifier = Modifier.padding(start = startP, top = topP, end = endP, bottom = bottomP)
    if (sizeX != null && sizeY != null) {
        modifier = modifier
            .size(sizeX, sizeY)
            .offset(newTransitionX, newTransitionY)
    } else if (size != null) {
        modifier = modifier.size(size)
    }

    // 조건에 따라 fillMaxSize 적용
    if (sizeX == null && sizeY == null && size == null) {
        modifier = modifier.fillMaxSize()
    }

    // 이미지 렌더링
    Image(
        painter = painter,
        contentDescription = contentKeyword,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun MoveActivityPreView() {
    Project2Theme {
        val coroutineScope = rememberCoroutineScope()
        MyPagerScreen(coroutineScope)
    }
}