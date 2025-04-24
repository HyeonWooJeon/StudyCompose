package com.example.project2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.alpha
import com.example.project2.ui.theme.Project2Theme
import java.time.LocalDate
import java.time.LocalTime

class Calendar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project2Theme {
                CalendarApp()
            }
        }
    }
}

@Composable
fun CalendarApp() {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    val today = LocalDate.now()

    val calendarDays by remember(currentMonth, selectedDate) {
        derivedStateOf { makeDate(currentMonth, selectedDate ?: LocalDate.now()) }
    }

    // 헤더 높이 애니메이션
    val headerHeight by animateDpAsState(if (selectedDate == null) 150.dp else 0.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .animateContentSize() // 레이아웃 전환 시 부드럽게 처리
    ) {
        // 상단 헤더 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
        ) {
            if (selectedDate == null) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "영상통화 예약하기",
                        modifier = Modifier.align(CenterHorizontally),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.cg_100)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "예약일 선택",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.cg_90)
                    )

                    Text(
                        text = "영상 통화 예약은 10일 이내의 날짜만 신청할 수 있습니다.",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.cg_80)
                    )
                }
            }
        }

        // 캘린더 헤더
        CalendarHeader(
            currentMonth = currentMonth,
            onMonthChange = { delta ->
                val newMonth = currentMonth.plusMonths(delta.toLong())
                if (!newMonth.isBefore(today.withDayOfMonth(1))) {
                    currentMonth = newMonth
                }
            },
            today = today,
            selectedDate = selectedDate
        )

        weekList()

        // 조건별로 다른 UI를 `Crossfade`로 전환
        Crossfade(targetState = selectedDate) { date ->
            if (date == null) {
                CalendarView(
                    selectedDate = selectedDate,
                    calendarDays = calendarDays,
                    today = today,
                    onDayClick = { selectedDate = it }
                )
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SelectedWeekView(
                        selectedDate = date,
                        calendarDays = calendarDays,
                        today = today,
                        onDayClick = { selectedDate = it }
                    )
                    prisoner()

                    TimeSelector(
                        selectedDate = date,
                        selectedTime = selectedTime,
                        onTimeSelect = { time -> selectedTime = time }
                    )
                }
            }
        }
    }

    // BackHandler로 날짜 선택 초기화
    BackHandler(enabled = selectedDate != null) {
        selectedDate = null
    }
}


@Composable
fun SelectedWeekView(selectedDate: LocalDate?,
                     calendarDays: List<CalendarDay>,
                     today: LocalDate?,
                     onDayClick: (LocalDate) -> Unit) {
    val selectedWeek = calendarDays.chunked(7).firstOrNull { week ->
        week.any { it.date == selectedDate }
    } ?: emptyList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(selectedWeek) { day ->
            if (today != null) {
                CalendarDayView(
                    day = day,
                    today = today,
                    onDayClick = onDayClick,
                    selectedDate = selectedDate
                )
            }
        }
    }
}

//주간 뷰에대한 내용
@Composable
fun CalendarDayView(
    day: CalendarDay,
    today: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    selectedDate: LocalDate?
) {
    val isWeekend = day.date.dayOfWeek.value % 7 == 0 || day.date.dayOfWeek.value % 7 == 6

    Box(
        modifier = Modifier
            .padding(4.dp)
            //가로세로 비율 1:1로 정사각형 모양 제공 selectedWeekView에 7가 제곻됨으로 1/7
            .aspectRatio(1f)
            .clip(RoundedCornerShape(50.dp))
            .background(if (day.date == selectedDate) colorResource(id = R.color.bl_100) else Color.Transparent)
            .clickable(
                enabled = !isWeekend && day.isCurrentMonth && day.date >= today,
                onClick = { onDayClick(day.date) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (day.isCurrentMonth) day.date.dayOfMonth.toString() else "",
            color = when {
                day.date == selectedDate-> colorResource(id = R.color.white_100)
                isWeekend -> colorResource(id = R.color.cg_30)
                day.isCurrentMonth && day.date < today -> Color.LightGray
                else -> Color.Gray
            },
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TimeSelector(selectedDate: LocalDate, selectedTime: LocalTime?, onTimeSelect: (LocalTime)->Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
    ){
        //오전 타임
        val amTimes = (9..11).flatMap { hour -> listOf(LocalTime.of(hour,0),LocalTime.of(hour,30)) }

        //오후 타임
        val pmTimes = (13..15).flatMap { hour -> listOf(LocalTime.of(hour,0),LocalTime.of(hour,30)) }

        Text(text = "예약시간 선택",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(
            id = R.color.cg_90),
            modifier = Modifier.padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.padding(top = 12.dp))

        Text(text = "오전",
            fontSize = 14.sp,
            color = colorResource(
            id = R.color.cg_90),
            modifier = Modifier.padding(start = 15.dp)
        )

        LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(8.dp)){
            items(amTimes){time ->
                Box( modifier = Modifier
                    .padding(6.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (time == selectedTime) colorResource(id = R.color.bl_100) else colorResource(
                            id = R.color.wt_100
                        )
                    )
                    .clickable {
                        onTimeSelect(time)
                        Log.d("Checktime:", "Selected Time: $time")
                    }
                    .padding(8.dp),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = String.format("%02d:%02d", time.hour, time.minute),
                        color = if (time == selectedTime) colorResource(id = R.color.wt_100) else colorResource(id = R.color.cg_80)
                    )
                }
            }
        }
        Text(text = "오후",
            fontSize = 14.sp,
            color = colorResource(
                id = R.color.cg_90),
            modifier = Modifier.padding(start = 15.dp)
        )

        LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(8.dp)){
            items(pmTimes){time ->
                Box( modifier = Modifier
                    .padding(6.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (time == selectedTime) colorResource(id = R.color.bl_100) else colorResource(
                            id = R.color.wt_100
                        )
                    )
                    .clickable {
                        onTimeSelect(time)
                        Log.d("Checktime:", "Selected Time: $time")
                    }
                    .padding(8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = String.format("%02d:%02d", time.hour, time.minute),
                        color = if (time == selectedTime) colorResource(id = R.color.wt_100) else colorResource(id = R.color.cg_80)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if(selectedTime?.hour == null && selectedTime?.minute == null){
                    Toast.makeText( context, "날짜를 선택해 주세요.",Toast.LENGTH_SHORT).show()
                } else {
                Toast.makeText( context,
                    "${selectedDate.year}년 ${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일 ${selectedTime?.hour}:${if(selectedTime?.minute == 0) "00" else selectedTime?.minute} 에 예약되었습니다", Toast.LENGTH_SHORT).show()            }
                },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.bl_100), // 원하는 색상으로 변경
                contentColor = Color.White // 글자 색상
            )
        ) {
            Text("확인", fontSize = 16.sp)
        }
    }
}


@Composable
fun CalendarHeader(currentMonth: LocalDate, onMonthChange: (Int) -> Unit, today: LocalDate, selectedDate: LocalDate? ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
                horizontalArrangement = if(selectedDate == null){Arrangement.SpaceBetween} else {Arrangement.Center},
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selectedDate == null) {
            Icon(
                painter = painterResource(id = R.drawable.icn_arrow),
                contentDescription = "",
                modifier = Modifier
                    .rotate(90f)
                    .clickable(enabled = selectedDate == null, onClick = { onMonthChange(-1) })
                    .background(
                        color =
                        //오늘 날짜 월이 해당 월이랑 같다면 버튼 색상을 어둡게 바꾼다. 라는 로직 추가 + 연도 비교도 해야지 정상작동
                        if (currentMonth.monthValue == today.monthValue && currentMonth.year == today.year) colorResource(
                            id = R.color.cg_20
                        ) else colorResource(
                            id = R.color.bl_100
                        ), shape = RoundedCornerShape(50.dp)
                    ), tint = Color.White
            )
        }
        Text(
            text = "${currentMonth.year}. ${currentMonth.monthValue}",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.cg_90),
        )
        if (selectedDate == null) {
            Icon(
                painter = painterResource(id = R.drawable.icn_arrow),
                contentDescription = "",
                modifier = Modifier
                    .rotate(-90f)
                    .clickable(enabled = selectedDate == null, onClick = { onMonthChange(1) })
                    .background(
                        color = colorResource(id = R.color.bl_100),
                        shape = RoundedCornerShape(50.dp)
                    ), tint = Color.White
            )
        }
    }
}

@Composable
fun CalendarView(calendarDays: List<CalendarDay>,today:LocalDate, onDayClick: (LocalDate) -> Unit, selectedDate:LocalDate?) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7), // 7열
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        items(calendarDays) { day ->
            //주말 val
            val isWeekend = day.date.dayOfWeek.value % 7 == 0 || day.date.dayOfWeek.value % 7 == 6
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (day.date == selectedDate) colorResource(id = R.color.bl_100) else Color.Transparent)
                    .clickable(
                        enabled = !isWeekend && day.isCurrentMonth && day.date >= today,
                        onClick = { onDayClick(day.date) }),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if(day.isCurrentMonth)day.date.dayOfMonth.toString() else "",
                    color = when {
                        today.isBefore(today) -> colorResource(id = R.color.cg_30)
                        isWeekend -> colorResource(id = R.color.cg_30)
                        day.isCurrentMonth && day.date < LocalDate.now() -> Color.LightGray
                        else -> colorResource(id = R.color.bl_100)
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
//수용자 임시로 만듦 실제는 DB 이용
@Composable
fun prisoner(){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 20.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "수용자 정보", color = colorResource(id = R.color.cg_90), fontWeight = FontWeight.Medium, fontSize = 16.sp)
            Text(text = "변경하기 >",color = colorResource(id = R.color.cg_70), fontWeight = FontWeight.Medium, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "김수용",color = colorResource(id = R.color.cg_90), fontWeight = FontWeight.Medium, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "경북북부제1교도소",color = colorResource(id = R.color.cg_80), fontWeight = FontWeight.Normal, fontSize = 12.sp)
                Text(text = "[000000]",color = colorResource(id = R.color.cg_80), fontWeight = FontWeight.Normal, fontSize = 12.sp)
            }

            Column(modifier = Modifier
                .height(40.dp)
                .padding(end = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "잔여횟수",color = colorResource(id = R.color.cg_80), fontWeight = FontWeight.Normal, fontSize = 12.sp)
                Text(text = "20/20",color = colorResource(id = R.color.cg_90), fontWeight = FontWeight.Medium, fontSize = 20.sp)
            }
        }
    }
}
//그냥 월화수목금토일 고정값
@Composable
fun weekList(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 25.dp, end = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        val weekDays = mutableListOf("일","월","화","수","목","금","토")
        for(days in weekDays){
            Text(text = days,fontWeight = FontWeight.SemiBold, color = colorResource(id = R.color.cg_80))
        }
    }
}

// 캘린더 날짜 함수
fun makeDate(currentMonth: LocalDate, selectedDate: LocalDate): List<CalendarDay> {
    val firstDayOfMonth = currentMonth.withDayOfMonth(1) // 현재 월의 첫 번째 날짜를 가져옴
    val totalDaysInMonth = currentMonth.lengthOfMonth()// 현재 월의 총 일수를 계산
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7// 현재 월의 첫 번째 날이 무슨 요일인지 확인

    val remainingDays = 42 - (startDayOfWeek + totalDaysInMonth)

    val prevDays = (1..startDayOfWeek).map { day ->
        CalendarDay(currentMonth.withDayOfMonth(day),
            isCurrentMonth = false)
    }

    val currentDays = (1..totalDaysInMonth).map { day ->
        CalendarDay(currentMonth.withDayOfMonth(day),
            isCurrentMonth = true,
            isSelected = selectedDate.dayOfMonth == day)
    }

    val nextDays = (1..remainingDays).map { day ->
        CalendarDay(currentMonth.withDayOfMonth(day),
            isCurrentMonth = false)
    }

    return prevDays + currentDays + nextDays
}

//캘린더 data 포멧
data class CalendarDay(
    val date: LocalDate, // 날짜
    val isCurrentMonth: Boolean, // 현재 월에 속하는지 여부
    val isSelected: Boolean = false // 선택된 날짜인지 여부
)

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    Project2Theme {
        CalendarApp()
    }
}