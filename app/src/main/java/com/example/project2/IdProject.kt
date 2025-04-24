package com.example.project2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme

class IdProject : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project2Theme {
                Surface(
                ) {
                    val viewModel = IdProjectModel()
                    IdProjectScreen(viewModel)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdProjectScreen(viewModel: IdProjectModel) {
    var sheetState = rememberModalBottomSheetState(true)
    if (viewModel.bottomSheet)
        bottomSheet(viewModel,onDismissRequest = { viewModel.onBottomSheetDismiss() }, sheetState = sheetState)

    val transition = rememberInfiniteTransition()
    val offsetTransition by transition.animateFloat(
        initialValue = 0f,
        targetValue = 79f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "신분증과 가족관계증명서를\n준비해주시기 바랍니다.",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                )
            )

            Text(
                text = "영상통화를 위해 민원인의 실명을 확인할 수 있는\n주민등록증, 운전면허증 등의 신분증이 필요합니다.",
                modifier = Modifier.padding(top = 16.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            )

            Box(modifier = Modifier
                .padding(top = 110.dp)
                .size(276.dp, 174.dp)
                ) {

            Image(
                painter = painterResource(id = R.drawable.img_ocr),
                contentDescription = "iconImage",
                modifier = Modifier

            )
                Box(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .offset(y = offsetTransition.dp)
                        .size(276.dp, 1.dp) // 스캔 라인의 크기 설정
                        .background(color = colorResource(id = R.color.bl_60))
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.onBottomSheetClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                        ,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.bl_100)),
            ) {
                Text(
                    text = "신분증 촬영",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomSheet(viewModel: IdProjectModel, sheetState: SheetState, onDismissRequest: () -> Unit) {

    //모달 내부 버튼 onClick시 Activity 이동
    val context = LocalContext.current

    //Intent로 다음 Activity 전환
    fun navigateToIdProjectCheck() {
        val intent = Intent(context, IdProjectCheck::class.java)
        context.startActivity(intent)
    }

    //modal
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.wt_100))
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(46.dp)
                        .height(6.dp)
                        .background(
                            color = colorResource(id = R.color.cg_20),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .align(Alignment.Center)
                ) {
                    BottomSheetDefaults.DragHandle(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },
        modifier = Modifier.wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
        ) {
        Column(
            modifier = Modifier
                .background(color = colorResource(id = R.color.wt_100))
                .padding(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 30.dp),

        ) {
            Text(text = "AI화상접견 서비스를 이용하려면\n이용약관에 동의가 필요합니다.",
                fontSize = 20.sp,
                color = colorResource(id = R.color.cg_100),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start)

            Spacer(modifier = Modifier.height(41.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
                    , horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "전체동의",
                    fontSize = 18.sp, fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.cg_90))

                    CheckBoxCheck(
                        checkedImage = if(viewModel.checkedState) painterResource(id = R.drawable.btn_checkbox )
                        else painterResource( id = R.drawable.btn_uncheckbox ),
                        checked = viewModel.checkedState,
                        onCheckedChange = {
                            viewModel.checkedState = it
                            viewModel.updateCheckedState(it) }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, top = 17.dp)
                        .background(
                            color = colorResource(id = R.color.cg_10),
                            shape = RoundedCornerShape(8.dp)
                        )

                ) {
                    rowForm(
                        text = "주민등록증 확인서비스 이용약관",
                        checked = viewModel.listCheckState[0],
                        onCheckedChange = { isChecked ->
                            viewModel.listCheckState = viewModel.listCheckState.toMutableList().apply {
                                set(0, isChecked)
                            }
                            viewModel.checkedState = viewModel.listCheckState.all { it }
                        }
                    )
                    rowForm(
                        text = "주민등록증 확인서비스 개인정보 수집및 이용동의",
                        checked = viewModel.listCheckState[1],
                        onCheckedChange = { isChecked ->
                            viewModel.listCheckState = viewModel.listCheckState.toMutableList().apply {
                                set(1, isChecked)
                            }
                            viewModel.checkedState = viewModel.listCheckState.all { it }
                        }
                    )
                    rowForm(
                        text = "주민등록증 모바일 확인서비스 안면인증 개인정보 수집/이용약관",
                        checked = viewModel.listCheckState[2],
                        onCheckedChange = { isChecked ->
                            viewModel.listCheckState = viewModel.listCheckState.toMutableList().apply {
                                set(2, isChecked)
                            }
                            viewModel.checkedState = viewModel.listCheckState.all { it }
                        }
                    )
                    rowForm(
                        text = "안면인증 기능 제공을 위한 민감정보(특징정보) 수집 및 이용동의",
                        checked = viewModel.listCheckState[3],
                        onCheckedChange = { isChecked ->
                            viewModel.listCheckState = viewModel.listCheckState.toMutableList().apply {
                                set(3, isChecked)
                            }
                            viewModel.checkedState = viewModel.listCheckState.all { it }
                        }
                    )
                }

            Button(
                onClick = { if (viewModel.listCheckState.all { it }) navigateToIdProjectCheck() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = viewModel.listCheckState.all { it },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.bl_100),
                    disabledContainerColor = colorResource(id = R.color.bl_30),
                    disabledContentColor = colorResource(id = R.color.wt_100)
                )
            ) {
                Text(
                    text = "동의",
                    color = colorResource(id = R.color.wt_100),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
//세부항목 약관 리스트
fun rowForm(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "[필수]",
                color = colorResource(id = R.color.rd_100),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .align(Alignment.Top)
            )

            Text(
                text = text+" >",
                color = colorResource(id = R.color.cg_70),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            )
        }

            CheckBoxCheck(
                modifier = Modifier.padding(start = 12.dp),
                checkedImage = if (checked) painterResource(id = R.drawable.btn_checkbox) else painterResource(id = R.drawable.btn_uncheckbox),
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }




//check박스
@Composable
fun CheckBoxCheck(
    checkedImage: Painter,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }

    Image(
        painter = checkedImage,
        contentDescription = "Checkbox",
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null // 기본 클릭 효과 제거
            ) {
                onCheckedChange(!checked)
            }
    )
}



@Preview(showBackground = true)
@Composable
fun IdProjectPreview() {
    Project2Theme {
        val viewModel = IdProjectModel()
        IdProjectScreen(viewModel)
    }
}
