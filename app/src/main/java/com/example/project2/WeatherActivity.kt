package com.example.project2


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.project2.api.NetworkResponse
import com.example.project2.api.WeatherModel
import com.example.project2.ui.theme.Project2Theme

class WeatherActivity : ComponentActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001 // 요청 코드 정의
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        // 권한 체크 및 요청
        if (!weatherViewModel.checkLocationPermission(this)) {
            weatherViewModel.requestLocationPermission(this, LOCATION_PERMISSION_REQUEST_CODE)
        }
//        else {
//            weatherViewModel.getLastLocation()
//            weatherViewModel.requestWeatherDataFromLocation(longitude = 0.0, latitude = 0.0)
//        }


        setContent {
            Project2Theme {
                WeatherView(weatherViewModel)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherView(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    var dropDownState by remember { mutableStateOf(false) }
    val weatherResult = viewModel.weatherViewResult.observeAsState()
    val saveSearchWordWord = viewModel.searchWords.observeAsState(emptyList())

    fun handleDone(){
        if (city.isNotBlank()) {
            viewModel.getData(city)
            viewModel.addSearchWord(city)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.wt_100))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "위치별 날씨 검색",
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    expanded = dropDownState,
                    onExpandedChange = { dropDownState = it },
                    modifier = Modifier.weight(1f) // OutlinedTextField가 가능한 넓이만큼 차지하게 함
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        value = city,
                        onValueChange = {
                            city = it
                            dropDownState = false
                        },
                        label = { Text("지역 검색") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownState)
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),

                    )

                    ExposedDropdownMenu(
                        expanded = dropDownState,
                        onDismissRequest = { dropDownState = false }
                    ) {
                        saveSearchWordWord.value.forEach { searchWord ->
                            DropdownMenuItem(
                                onClick = {
                                    city = searchWord.searchWord
                                    dropDownState = false
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = searchWord.searchWord)

                                    IconButton(
                                        onClick = {
                                            viewModel.deleteSearchWord(searchWord)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "삭제"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                IconButton(
                    onClick = {

                        handleDone()
                    },
                    modifier = Modifier
                        .padding(start = 8.dp) // OutlinedTextField와 IconButton 사이에 간격 추가
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "검색"
                    )
                }
                IconButton(
                    onClick = {
                viewModel.getLastLocation()
                    },
                    modifier = Modifier
                        .padding(start = 8.dp) // OutlinedTextField와 IconButton 사이에 간격 추가
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "GPS"
                    )
                }
            }

            when(val result = weatherResult.value) {
                is NetworkResponse.Error -> {
                    Text(text = result.message)
                }
                NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }
                is NetworkResponse.Success -> {
                    LoadUI(data = result.data)
                }
                null -> {}
            }
        }
    }
}

@Composable
    fun LoadUI(data: WeatherModel) {
        val weatherImage = data.current.condition.icon
        Column(
            modifier = Modifier
                .fillMaxWidth() // 전체 화면을 채우도록 설정
                .padding(16.dp)
                .border(
                    0.5.dp,
                    color = colorResource(id = R.color.cg_50),
                    shape = RoundedCornerShape(5.dp)
                )
                .background(
                    color = colorResource(id = R.color.cg_10),
                    shape = RoundedCornerShape(5.dp)
                ),
            verticalArrangement = Arrangement.Center, // 상하 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 좌우 중앙 정렬,

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically, // 각 Row 요소를 중앙 정렬
                horizontalArrangement = Arrangement.SpaceEvenly // Row 내부에서 요소 간 공간을 고르게 분배
            ) {

                AsyncImage(
                    model = "https:$weatherImage".replace("64x64", "128x128"),
                    contentDescription = "Weather Image",
                    modifier = Modifier.size(160.dp) // 이미지 크기 설정
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
//            horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 온도
                    Text(
                        text = "${data.current.temp_c} °C",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.cg_90)
                    )
                    // 날씨
                    Text(
                        text = data.current.condition.text,
                        modifier = Modifier.padding(4.dp), fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.cg_90)
                    )
                    // 지역 이름
                    Row() {
                        Text(
                            text = data.location.name,
                            modifier = Modifier.padding(4.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location Icon"
                        )
                    }
                    Text(
                        text = data.location.country,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.cg_70),
                    )
                }
            }

            // 추가 정보들 (자외선 지수, 풍속 등)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp, bottom = 15.dp, start = 15.dp)
                    .border(
                        0.5.dp, color = colorResource(
                            id = R.color.cg_50
                        ), shape = RoundedCornerShape(5.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "UV : ${data.current.uv}",
                        modifier = Modifier
                            .weight(1f) // 각 Text가 동일한 공간을 차지하게 함
                            .padding(start = 8.dp, 2.dp),
                        fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.cg_90)

                    )
                    Text(
                        text = "풍속 : ${data.current.wind_mph} mph",
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp),
                        fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.cg_90)

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "습도 : ${data.current.humidity}%",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, 2.dp),
                        fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.cg_90)

                    )
                    Text(
                        text = "현재 시간 : ${data.location.localtime}",
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp),
                        fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.cg_90)

                    )
                }
            }
        }
    }

//@Preview
//@Composable
//fun WeatherPreview(){
//    Project2Theme {
//
//        weatherView(weatherViewModel)
//    }
//}