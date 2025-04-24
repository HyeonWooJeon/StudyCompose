package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project2.ui.theme.Project2Theme
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId
import java.io.File


class RealmActivity : ComponentActivity() {
    private lateinit var backgroundRealm:  Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(this)

        //Realm파잍 삭제
//        val realmFile = File(getFilesDir(), "myrealm.realm")
//        if (realmFile.exists()) {
//            realmFile.delete()
//        }

        val config = RealmConfiguration.Builder()
            .name("myrealm.realm")
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded() // 개발 시 사용, 프로덕션에서 제거
                //ui 스레드 쓰기 허용
            .allowWritesOnUiThread(true)
//          .modules(MyRealmModule())
            .build()

        Realm.setDefaultConfiguration(config)
        backgroundRealm = Realm.getDefaultInstance()

        setContent {
            Project2Theme {
                Surface {
                    RealmScreen(backgroundRealm)
                }
            }
        }
    }

    override fun onDestroy() {
        backgroundRealm.close()
        super.onDestroy()
    }
}

@Composable
fun RealmScreen(backgroundRealm: Realm) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }

    val realm = remember { Realm.getDefaultInstance() }
//    var users by remember { mutableStateOf( listOf<RealmUser>() ) }
//    users = realm.where<RealmUser>().findAll()
    var users = remember { backgroundRealm.where<RealmUser>().findAll() }

    DisposableEffect(Unit) {
        onDispose {
            realm.close()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("나이") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = pw,
            onValueChange = { pw = it },
            label = { Text("비밀번호") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        backgroundRealm.executeTransaction { transactionRealm ->
                            val userAge = age.toIntOrNull() // String을 Int로 변환, 실패 시 null 반환
                            if (userAge != null) { // 유효한 값일 경우에만 객체 생성
                                //  val primaryKeyValue = ObjectId()
                                transactionRealm.createObject(RealmUser::class.java).apply {
                                    this.name = name
                                    this.pw = pw
                                    this.age = userAge
                                }
                                // UI 스레드에서 사용자 데이터 업데이트
                                val updatedUsers = transactionRealm.where<RealmUser>().findAll()
                                users = updatedUsers
                            } else {
                                CHLog.e("RealmError", "Invalid age input: empty or null")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()  // Handle error
                        CHLog.e("RealmError", "Transaction failed: ${e.message}")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(users.size) { idx ->
//                UserItem(users[idx])
                            users[idx]?.let { UserItem(it) }
            }
        }

    }

//    DisposableEffect(Unit) {
//        onDispose {
//
//        }
//    }
}

// kotlin SDK, Android Realm SDK 방식 둘다 사용됨
@Composable
fun UserItem(users: RealmUser) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(text = "ID: ${users.id}")
                Text(text = "Name: ${users.name}")
                Text(text = "Age: ${users.age}")
                Text(text = "Password: ${users.pw}")
            }
        }
    }
}

//Kotlin PreView
//@Preview(showBackground = true)
//@Composable
//fun RealmPreView() {
//    Project2Theme {
//        RealmScreen(Realm.open(RealmConfiguration.create(schema = setOf(RealmUser::class))))
//    }
//}


//기존 PreView
@Preview(showBackground = true)
@Composable
fun RealmPreView() {
    Project2Theme {
        //Realm.init(context)
//        RealmScreen()

    }

}
