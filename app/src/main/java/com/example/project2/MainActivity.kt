package com.example.project2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme
import java.util.Calendar


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userInfoRepository = UserInfoRepository(this)
        setContent {
            Project2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(userInfoRepository = userInfoRepository)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(modifier: Modifier = Modifier, userInfoRepository:UserInfoRepository) {

    //이름
    var name by remember { mutableStateOf("")}
    //생년월일
    var birth by remember { mutableStateOf("")}
    //카드 번호
    var cardNum by remember { mutableStateOf("")}
    //유효기간 카드
    var efDate by remember { mutableStateOf("")}
    //계정 비밀번호
    var pw by remember { mutableStateOf("")}
    //전화번호
    var phoneNum by remember { mutableStateOf("")}
    //이메일
    var email by remember { mutableStateOf("")}
    //안전결제 비밀번호
    var safeNum by remember { mutableStateOf("")}
    //안전결제 비밀번호 확인
    var safeNumCheck by remember { mutableStateOf("")}
    // 집 전화번호
    var homeNum by remember { mutableStateOf("")}
    //스크롤 변수 선언
    val scrollState = rememberScrollState()

    //키보드가 열릴 때 스크롤 동작을 처리
//    val nestedScrollConnection = remember {object :NestedScrollConnection{} }

    var nameFocus by remember { mutableStateOf(false) }
    var cardFocus by remember { mutableStateOf(false) }
    var pwFocus by remember { mutableStateOf(false) }
    var phoneNumFocus by remember { mutableStateOf(false) }
    var emailFocus by remember { mutableStateOf(false) }
    var homeNumFocus by remember { mutableStateOf(false) }

    //터치 area
    val namefocusRequester = remember { FocusRequester() }
    val birthfocusRequester = remember { FocusRequester() }
    val cardNumfocusRequester = remember { FocusRequester() }
    val efDatefocusRequester = remember { FocusRequester() }
    val pwfocusRequester = remember { FocusRequester() }
    val phoneNumfocusRequester = remember { FocusRequester() }
    val emailfocusRequester = remember { FocusRequester() }
    var safeNumfocusRequester = remember { FocusRequester() }
    val safeNumCheckfocusRequester = remember { FocusRequester() }
    val homeNumFocusRequester = remember { FocusRequester() }
    val buttonfocusRequester = remember {FocusRequester()}

    var nameError by remember { mutableStateOf(false) }
    var birthError by remember { mutableStateOf(false) }
    var cardNumError by remember { mutableStateOf(false) }
    var efDateError by remember { mutableStateOf(false) }
    var pwError by remember { mutableStateOf(false) }
    var phoneNumError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var safeNumError by remember { mutableStateOf(false) }
    var safeNumCheckError by remember { mutableStateOf(false) }
    var homeNumError by remember { mutableStateOf(false) }

    var nameErrorMessage by remember { mutableStateOf("") }
    var birthErrorMessage by remember { mutableStateOf("") }
    var cardNumErrorMessage by remember { mutableStateOf("") }
    var efDateErrorMessage by remember { mutableStateOf("") }
    var pwErrorMessage by remember { mutableStateOf("") }
    var phoneNumErrorMessage by remember { mutableStateOf("") }
    var emailErrorMessage by remember { mutableStateOf("") }
    var safeNumErrorMessage by remember { mutableStateOf("") }
    var homeNumErrorMessage by remember { mutableStateOf("") }


//    var usersInfo by remember { mutableStateOf(emptyList<UserInfo>()) }
    fun addUserInfo(){
        val userInfo = UserInfo(name= name, birth = birth, cardNum = cardNum.toLong(),
            efDate = efDate, pw = pw, phoneNum = phoneNum.toLong(), email = email,
            homeNum = homeNum.toIntOrNull()?:0, safeNum = safeNum)
        userInfoRepository.addUserInfo(userInfo){
        }
    }


    //카드 등록view 유효성 체크
    //email 유효성 체크
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    //pw 양식 유효성 체크 (영어랑 숫자랑 가능하도록 바꿈 최소 8글자 제한)
    fun isPasswordVaild(pw:String):Boolean {
        val patten = "^[a-zA-Z0-9]{8,}\$".toRegex()
//        val patten = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+\$).{8,}\$".toRegex()
        return pw.matches(patten)
    }
    //전화번호 양식 11자리
    fun isPhoneVaild(phoneNum: String):Boolean{
        val patten = "^[0-9]{9,11}\$".toRegex()
        return phoneNum.matches(patten)
    }
    fun isHomeNumValid(homeNum: String?): Boolean {
        // homeNum이 null 또는 빈 문자열인 경우 true
        if (homeNum.isNullOrEmpty()) return true

        // 모든 숫자 문자열을 허용
        val pattern = "^[0-9]*$".toRegex()
        return homeNum.matches(pattern)
    }
    //카드 양식 체크 16자리를 할까 하다 안함


    //birthDatePicker관련 변수
    val birthDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        birth = "$year.${monthOfYear + 1}.$dayOfMonth" // 날짜 포맷을 설정
        cardNumfocusRequester.requestFocus()
        birthError = false
    }
    //datePicker관련 변수
    val efDateDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        efDate = "$year.${monthOfYear + 1}.$dayOfMonth" // 날짜 포맷을 설정
        pwfocusRequester.requestFocus()
        efDateError = false
    }
    //context를 사용해 현재 Activity 리소스나 서비스 접근
    val context = LocalContext.current

    fun navigateToSubActivity() {
    //addUserInfo()
        val intent = Intent(context, SubActivity::class.java)
        intent.putExtra("key",name)
        context.startActivity(intent)
    }
    val calendar = Calendar.getInstance()

    //생년월일에대한 datePicker
    val birthDatePickerDialog = remember(context) {
        DatePickerDialog(
            context,
            birthDatePickerListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    //유효기간에 대한 datePicker
    val efDateDatePickerDialog = remember(context) {
        DatePickerDialog(
            context,
            efDateDatePickerListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val checkedImage = painterResource(id = R.drawable.btn_checkbox)
    val uncheckedImage = painterResource(id = R.drawable.btn_uncheckbox)

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
                ),
            ),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .size(width = 360.dp, height = 1274.dp)
        ,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
               , verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.icn_arrow),
                contentDescription = "Dropdown Arrow",
                modifier = Modifier
                    .size(40.dp)
                    .rotate(90f)
            )
            Text(
                text = "카드등록 및 부가서비스 이용동의",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.wt_100),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(40.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = colorResource(id = R.color.wt_100)
                )
                .weight(1f)
                .padding(20.dp)
              ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(modifier=Modifier
            .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(
                text = "카드를 등록해보세요.",
                modifier = modifier
                    .padding(top = 20.dp),
                style = TextStyle(fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.gg_80)),
            )
            Text(
                text = "카드를 등록하시면 통화요금 실시간 결제가 가능하고\n비밀번호 입력만으로 군인 사용자에게 포인트를 선물할 수 있어요.",
                modifier = modifier
                    .padding(top = 12.dp, bottom = 32.dp),
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.gg_80)),
                textAlign = TextAlign.Center
            )
            Text(
                text = "카드정보",
                modifier = modifier
                    .align(Alignment.Start)
                    .padding(bottom = 4.dp, start = 10.dp),
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal,
                    color =colorResource(id = R.color.gg_80)),
                )
            if(nameError){ Text(text = nameErrorMessage
                , color = colorResource(id = R.color.rd_100 ),
                fontSize = 10.sp)
            }
             textFieldForm(value = name,
                 modifier = Modifier
                     .onFocusChanged {
                         nameFocus= it.isFocused
                     },
                 placeholder = if(nameFocus){"입력중.."}else{"이름을 입력해주세요"},
                 focusRequester = namefocusRequester,
                 text = "이름",
                 action= ImeAction.Next,
                 onValueChange = { name = it
                     nameError= false },
                 onNextAction = { birthDatePickerDialog.show() },
                 keyboardType = KeyboardType.Text,
                 isError = nameError
             )
                if(birthError){ Text(text = birthErrorMessage
                    , color = colorResource(id = R.color.rd_100 ),
                    fontSize = 10.sp)
                }
            Box(modifier=Modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        birthDatePickerDialog.show()
                    }
                }
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .height(42.dp)
                    .background(
                        colorResource(id = R.color.gg_1),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        1.dp,
                        if (birthError) colorResource(id = R.color.rd_100) else colorResource(id = R.color.gg_10),
                        shape = RoundedCornerShape(50.dp),
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "생년월일",
                    modifier = Modifier
                        .padding(start = 16.dp, top = 12.dp, bottom = 11.dp)
                        .width(74.dp),
                    color = colorResource(id = R.color.gg_80),
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                )
                BasicTextField(
                    value = birth,
                    onValueChange = { birth = it 
                                    birthError = false},
                    enabled = false,
                    textStyle = TextStyle(fontSize = 14.sp,textAlign = TextAlign.Center),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 25.dp, top = 13.dp, bottom = 12.dp)
                        .height(17.dp)
                        .focusRequester(birthfocusRequester)
                )
                Icon(
                    painter = painterResource(id = R.drawable.icn_arrow),
                    contentDescription = "Dropdown Arrow",
                    tint = colorResource(R.color.gg_70)
                )
            }
            }
            if(cardNumError){ Text(text = cardNumErrorMessage
                , color = colorResource(id = R.color.rd_100 ),
                fontSize = 10.sp)
            }
            textFieldForm(value = cardNum,
                modifier = Modifier
                    .onFocusChanged {
                                    cardFocus= it.isFocused
                    },
                placeholder = if(cardFocus){"입력중.."}else{"카드번호를 입력해주세요"},
                focusRequester = cardNumfocusRequester,
                text = "카드번호",
                onValueChange = { cardNum = it
                                cardNumError = false},
                action = ImeAction.Next,
                onNextAction = {efDateDatePickerDialog.show()},
                keyboardType = KeyboardType.Number,
                isError = cardNumError
                )
            if(efDateError){ Text(text = efDateErrorMessage
                , color = colorResource(id = R.color.rd_100 ),
                fontSize = 10.sp)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .height(42.dp)
                    .background(
                        colorResource(id = R.color.gg_1),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        1.dp,
                        if (efDateError) colorResource(id = R.color.rd_100) else colorResource(id = R.color.gg_10),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures {
                            efDateDatePickerDialog.show()
                        }
                    },horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "유효기간",
                    modifier = Modifier
                        .padding(start = 16.dp, top = 12.dp, bottom = 11.dp)
                        .width(74.dp),

                    color = colorResource(id = R.color.gg_80),
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                )
                BasicTextField(
                    value = efDate,
                    onValueChange = { efDate = it 
                                    efDateError = false},
                    enabled = false,

                    textStyle = TextStyle(fontSize = 14.sp,textAlign = TextAlign.Center),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 25.dp, top = 13.dp, bottom = 12.dp)
                        .height(17.dp)
                        .focusRequester(efDatefocusRequester)
                                        )
                        Icon(
                            painter = painterResource(id = R.drawable.icn_arrow),
                            contentDescription = "Dropdown Arrow",
                            tint = colorResource(R.color.gg_70)
                        )
            }
            if(pwError){ Text(text =pwErrorMessage
                , color = colorResource(id = R.color.rd_100 ),
                fontSize = 10.sp)
            }
            textFieldForm(
                modifier=Modifier
                    .onFocusChanged {
                        pwFocus = it.isFocused
                    },
                value = pw,
                placeholder = if(pwFocus){"입력중.."}else{"비밀번호를 입력해주세요"},
                focusRequester = pwfocusRequester,
                text = "비밀번호",
                action = ImeAction.Next,
                onValueChange = {pw = it
                    pwError = false},
                onNextAction = {phoneNumfocusRequester.requestFocus()},
                keyboardType = KeyboardType.Text,
                isError = pwError,

            )
            Spacer(modifier = Modifier.height(4.dp))
            if(phoneNumError){ Text(text = phoneNumErrorMessage
                , color = colorResource(id = R.color.rd_100 ),
                fontSize = 10.sp)
            }
            textFieldForm(
                modifier = Modifier
                    .onFocusChanged {
                    phoneNumFocus = it.isFocused
                    },
                value = phoneNum,
                placeholder = if(phoneNumFocus){"입력중.."}else{"전화번호를 입력해주세요"},
                focusRequester = phoneNumfocusRequester,
                text = "전화번호",
                action = ImeAction.Next,
                onValueChange = {phoneNum = it
                                phoneNumError = false},
                onNextAction = {emailfocusRequester.requestFocus()},
                keyboardType = KeyboardType.Phone,
                isError = phoneNumError
            )
            if(emailError){ Text(text = emailErrorMessage
                , color = colorResource(id = R.color.rd_100 ),
                fontSize = 10.sp)
            }
            textFieldForm(value = email,
                modifier = Modifier
                    .onFocusChanged {
                                    emailFocus= it.isFocused
                    },
                placeholder = if(emailFocus)"입력중.." else{"이메일을 입력해주세요"},
                focusRequester = emailfocusRequester,
                text = "이메일",
                action = ImeAction.Next,
                onValueChange = {email = it
                                emailError = false},
                onNextAction = { safeNumfocusRequester.requestFocus() },
                keyboardType = KeyboardType.Text,
                isError = emailError
            )
            Column(modifier = modifier
                .pointerInput(Unit){
                    detectTapGestures {
                        safeNumfocusRequester.requestFocus()
                    }
                },
            ){
                Text(
                    text = "안전결제 비밀번호 설정",
                    modifier = modifier
                        .padding(top = 20.dp, bottom = 4.dp, start = 10.dp),
                    style = TextStyle(fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.gg_70))
                )

                if(safeNumError){ Text(text = safeNumErrorMessage
                    , color = colorResource(id = R.color.rd_100 ),
                    fontSize = 10.sp)
                }
                safeNumPayment(value = safeNum,
                    placeholder = "숫자 4자리를 입력해주세요",
                    focusRequester = safeNumfocusRequester,
                    action = ImeAction.Next,
                    onValueChange = { safeNum = it
                        safeNumError = false},
                    onNextAction = {safeNumCheckfocusRequester.requestFocus()},
                    isError = safeNumError

                    )
                safeNumPayment(value = safeNumCheck,
                    placeholder = "비밀번호를 한번 더 입력해주세요",
                    focusRequester= safeNumCheckfocusRequester,
                    action = ImeAction.Next,
                    onValueChange = { safeNumCheck = it
                        safeNumCheckError = false},
                    onNextAction = {homeNumFocusRequester.requestFocus()},
                    isError = safeNumCheckError,
                )
                }
                Text(
                    text = "안전한 결제 이용을 위해 비밀번호를 등록해주세요.",
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 10.dp, bottom = 32.dp),
                    style = TextStyle(fontSize = 12.sp,

                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.rd_90)),
                    )

                Text(text = "부가서비스 이용안내",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.gg_80))

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                    , verticalAlignment = Alignment.CenterVertically
                ) {
                    checkBoxCheck(
                        checkedImage = checkedImage,
                        uncheckedImage = uncheckedImage
                    )
                    Text(text = "[선택]",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.gg_60))


                    Text(text = "부가서비스 이용에 동의합니다.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.gg_80))

                    Spacer(modifier = modifier.size(35.dp))
                }
    Column(
    modifier= Modifier
        .fillMaxWidth()
        .height(204.dp)
        .border(
            1.dp,
            colorResource(id = R.color.gg_10),
            shape = RoundedCornerShape(20.dp),
        )
    ) {
        logoForm(text = "전화걸기", textSub = "부과서비스에 가입하시면 저렴한 요금으로 공중전화 사용자에게 영상/음성전화를 걸 수 있어요.", painter = painterResource(id = R.drawable.group_58))
        logoForm(text = "미디어 파일 전송", textSub = "문자대화중 사진 및 동영상 파일을 전송 할 수 있어요.", painter = painterResource(id = R.drawable.group_60))
        logoForm(text = "콜렉트콜 전화 수신", textSub = "수신자 부담 전화가 걸려왔을때 받을 수 있어요.", painter = painterResource(id = R.drawable.group_61))
    }
            Spacer(modifier = modifier.size(4.dp))
    if (homeNumError){ Text(text = homeNumErrorMessage
        , color = colorResource(id = R.color.rd_100 ),
        fontSize = 10.sp)

    }
            textFieldForm(
                value = homeNum,
                modifier=Modifier
                    .onFocusChanged {
                        homeNumFocus = it.isFocused
                    },

                placeholder = if(homeNumFocus){"입력중.."}else{"집 전화번호를 입력해주세요"},
                focusRequester = homeNumFocusRequester,
                text = "집전화[선택]",
                action = ImeAction.Done,
                onValueChange = {homeNum = it},
                onNextAction = {buttonfocusRequester.requestFocus()},
                keyboardType = KeyboardType.Number,
                fontSize = 14.sp,
                isError = homeNumError
            )

            Text(text = "부가서비스는 통화 및 문자 사용료 외 별도의 기본료가 없어요.\n" +
                    "부가서비스에 동의하지 않아도 전화 수신과 문자메세지, 포인트 선물하기 이용에는 문제가 없으며, 언제든지 신청 할 수 있어요.",
                fontSize = 12.sp,
                color = colorResource(id = R.color.gg_30),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                lineHeight = 16.8.sp,
                letterSpacing = -0.24.sp
                )

            Text(text = "(주)그린비파트너스\n서울특별시 강남구 도곡로 222 4층 대표:함돈경\n사업자번호 201-86-40104 전화번호 070-7331-6202",
                fontSize = 8.sp,
                color = colorResource(id = R.color.gg_30),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 40.dp),
            lineHeight = 11.2.sp,
            letterSpacing = -0.16.sp)
            }
        }

        Spacer(modifier = modifier.height(10.dp))

                nextButton(
//                    name,pw,birth,
//                    cardNum,efDate,phoneNum,
//                    email,safeNum,homeNum,
                    click = {
                        if( //이름 공백, 숫자 거르기
                        name.isEmpty() || name.any{it.isDigit()}){
                            nameError = true
                            nameErrorMessage ="올바른 이름을 입력해주세요."
                            namefocusRequester.requestFocus()
                        }else if( //생일 공백 거르기
                            birth.isEmpty()) {
                            birthError = true
                            birthErrorMessage = "날짜를 선택해주세요."
                            birthDatePickerDialog.show()
                            
                        }else if(//카드번호 공백, 숫자가아닌무언가, 15자이하 거르기
                            cardNum.isEmpty()||!cardNum.any{it.isDigit()}||cardNum.length<15) {
                            cardNumError = true
                            cardNumErrorMessage = "카드번호는 16자이상 숫자만 입력해주세요"
                            cardNumfocusRequester.requestFocus()

                        }else if(//유효기간 공백 거르기
                            efDate.isEmpty()) {
                            efDateError = true
                            efDateErrorMessage = "날짜를 선택해주세요."
                       efDateDatePickerDialog.show()

                        } else if(//비밀번호 유효성함수 체크
                            !isPasswordVaild(pw)){
                            pwError = true

                            pwErrorMessage = "영어, 숫자를 조합하여 8자 이상 입력해주세요."
                            pwfocusRequester.requestFocus()
                        }

                    else if(//전화번호 숫자가아닌 것들 번호 유효성함수 체크
                            !phoneNum.any{it.isDigit()}||!isPhoneVaild(phoneNum)){
                            phoneNumError = true
                            phoneNumErrorMessage = "전화번호는 숫자만 입력해주세요."
                            phoneNumfocusRequester.requestFocus()

                        }else if (// 이메일 유효성 체크
                            !isValidEmail(email)) {
                            emailError = true
                            emailErrorMessage = "이메일 형식이 맞지 않습니다."
                            emailfocusRequester.requestFocus()

                        }else if(//안전결제 안전결제체크 다를경우, 안전번호가 4자리이하일경우
                            safeNum != safeNumCheck||safeNum.length<3) {
                            safeNumError = true
                            safeNumCheckError = true
                            safeNumErrorMessage = "입력하신 비밀번호가 다릅니다."
                            safeNumfocusRequester.requestFocus()

                        }else if(//안전결제 안전결제체크 다를경우, 안전번호가 4자리이하일경우
                          !isHomeNumValid(homeNum)){
                            homeNumError = true
                            homeNumErrorMessage = "집전화 번호는 숫자만 입력해주세요."
                            homeNumFocusRequester.requestFocus()

                        }
                        else{
                                val userInfo = UserInfo(name= name, birth = birth, cardNum = cardNum.toLong(),
                                    efDate = efDate, pw = pw, phoneNum = phoneNum.toLong(), email = email,
                                    homeNum = homeNum.toIntOrNull()?:0, safeNum = safeNum)
                                userInfoRepository.addUserInfo(userInfo){
                                    navigateToSubActivity()
                                }


                        }
                    })

        Spacer(modifier = modifier.height(10.dp))
        }
    }


@Composable
fun checkBoxCheck(
    checkedImage: Painter,
    uncheckedImage: Painter,
    initialCheck: Boolean = false,
    modifier: Modifier = Modifier,

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
fun  logoForm(text: String,textSub:String,painter: Painter){
    Row(
        modifier = Modifier
    ) {
        Image(painter = painter,
            contentDescription = "phoneBlueIcon",
//        tint = colorResource(R.color.wt_100),
            modifier = Modifier
                .size(52.dp)
                .padding(start = 12.dp, top = 12.dp, end = 10.dp)
        )
        Column(
        ) {
            Text(text = text,
                fontSize = 16.sp,
                modifier=Modifier.padding(top = 12.dp),
                color = colorResource(id = R.color.gg_80),
                fontWeight = FontWeight.SemiBold            )

            Text(text = textSub,
                fontSize = 12.sp,
                color = colorResource(id = R.color.gg_70),
                modifier=Modifier.padding(end = 12.dp),
                lineHeight = 18.sp,
                letterSpacing = -0.48.sp

            )
        }
    }
}

@Composable
fun textFieldForm(modifier: Modifier = Modifier,
                  value: String,
                  placeholder: String,
                  focusRequester: FocusRequester,
                  text:String,
                  isError:Boolean,
                  onValueChange: (String) -> Unit,
                  action: ImeAction,
                  onNextAction: () -> Unit,
                  keyboardType: KeyboardType,
                  fontSize: TextUnit = 16.sp
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .height(42.dp)
            .background(
                colorResource(id = R.color.gg_1),
                shape = RoundedCornerShape(50.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures {
                    focusRequester.requestFocus()
                }
            }
            .border(
                1.dp,
                color = if (isError) colorResource(id = R.color.rd_100) else colorResource(id = R.color.gg_10),
                shape = RoundedCornerShape(50.dp),

                ),
        ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 16.dp, top = 12.dp, bottom = 11.dp)
                .width(74.dp),
            color = colorResource(id = R.color.gg_80),
            fontSize = fontSize,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.SemiBold,

            )
//
//        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier= Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center

        ) {
            if (value.isEmpty()) {
            Text(
                text = placeholder,
                fontSize = 10.sp,
                color = colorResource(id = R.color.gg_30),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .size(320.dp, 42.dp)
                    .padding(vertical = 12.dp)
            )
        }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center),
                modifier = modifier
                    .padding(start = 1.dp, end = 16.dp)
                    .height(17.dp)
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                visualTransformation= VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    imeAction = action,
                    keyboardType = keyboardType
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                        onNextAction()
                    })
            )
        }
    }
}
@Composable
fun safeNumPayment( value:String, placeholder: String,isError: Boolean, focusRequester: FocusRequester, onValueChange:(String)->Unit ,action:ImeAction,onNextAction:()->Unit){
    val focusManager = LocalFocusManager.current
    Box(
        modifier= Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .border(
                1.dp,
                if (isError) colorResource(id = R.color.rd_100) else colorResource(id = R.color.gg_10),
                shape = RoundedCornerShape(50.dp)
            )
            .background(
                colorResource(id = R.color.gg_1),
                shape = RoundedCornerShape(50.dp)
            )
        ,contentAlignment = Alignment.Center
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                fontSize = 10.sp,
                color = colorResource(id = R.color.gg_30),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 16.sp,textAlign = TextAlign.Center),
            modifier = Modifier
                .size(320.dp, 42.dp)
                .focusRequester(focusRequester)
                .padding(vertical = 12.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = action,
                keyboardType = KeyboardType.Decimal,
            ),keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                    onNextAction()
                }
        ))
    }
}

@Composable
fun nextButton(
//    name: String,
//    pw: String,
//    birth: String,
//    cardNum: String,
//    efDate: String,
//    phoneNum: String,
//    email: String,
//    safeNum: String,
//    safeNumCheck: String,
    click:() -> Unit
) {
    OutlinedButton(
        onClick = click,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            .height(60.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = colorResource(id = R.color.wt_100),
        disabledContainerColor = colorResource(id = R.color.rd_100)),
        shape = RoundedCornerShape(50.dp), // 버튼 모서리를 둥글게 설정
        border = BorderStroke(1.dp, color = colorResource(id = R.color.tq_100))
    ){
        Text(
            text = "등록하기",
            style = TextStyle(fontSize = 16.sp),
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun MyAppPreview() {
//    // Mock UserInfoRepository
//    val mockUserInfoRepository = object : UserInfoRepository(ContextWrapper(EmptyContext)) {
//        override fun addUserInfo(userInfo: UserInfo, callback: () -> Unit) {
//            // Mock implementation
//            callback()
//        }
//
//        override fun getAllUserInfo(callback: (List<UserInfo>) -> Unit) {
//            // Mock implementation
//            callback(emptyList())
//        }
//
//        override fun deleteUserInfo(userInfo: UserInfo, callback: () -> Unit) {
//            // Mock implementation
//            callback()
//        }
//    }
//
//    Project2Theme {
//        // Pass mock repository to MyApp
//        MyApp(userInfoRepository = mockUserInfoRepository)
//    }
//}
