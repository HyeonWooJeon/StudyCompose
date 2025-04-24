package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect


class DatabaseFlowActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepository
    private lateinit var userInfoRepository: UserInfoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRepository = UserRepository(this)
        userInfoRepository = UserInfoRepository(this)
        setContent {
            Project2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserInputScreen(userRepository, userInfoRepository)
                }
            }
        }
    }
}

@Composable
fun UserInputScreen(userRepository: UserRepository, userInfoRepository:UserInfoRepository) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var users by remember { mutableStateOf(emptyList<User>()) }
    var userInfo by remember { mutableStateOf(emptyList<UserInfo>()) }


    // 사용자 목록을 다시 갱신 (중요)
    fun refreshUsers() {
        userRepository.getAllUsers { fetchedUsers ->
            users = fetchedUsers
        }
    }

    fun refreshUserInfo(){
        userInfoRepository.getAllUserInfo{ fetchUserInfo ->
            userInfo = fetchUserInfo
        }
    }

    // 사용자 추가
    fun addUser() {
        val user = User(name = name, age = age.toIntOrNull() ?: 0, phone = phone)
        userRepository.addUser(user) {
            refreshUsers()  // 사용자 추가 후 목록 갱신
        }
    }

    // 사용자 삭제
    fun deleteUser(user: UserInfo) {
        userRepository.deleteUser(user) {
            refreshUsers()  // 사용자 삭제 후 목록 갱신
        }
    }

    LaunchedEffect(Unit){
        refreshUserInfo()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = if (name.isEmpty()) "이름" else name,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = age,
            onValueChange = { age = it },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = if (age.isEmpty()) "나이" else age,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = phone,
            onValueChange = { phone = it },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = if (phone.isEmpty()) "전화번호" else phone,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                addUser()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원 정보저장")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("저장된 user 정보", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(userInfo) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("이름: ${user.name}")
                        Text("생일: ${user.birth}")
                        Text("이메일: ${user.email}")
                    }
                    Button(
                        onClick = {
                            deleteUser(user)
                        }
                    ) {
                        Text("삭제")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInputScreenPreview() {
    Project2Theme {
        UserInputScreen(UserRepository(context = LocalContext.current),
            UserInfoRepository(context = LocalContext.current)
        )
    }
}