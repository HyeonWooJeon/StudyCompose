package com.example.project2

import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.lang.Exception

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CHLog.d("ToKEN", "Refreshed token: $token")

        // 여기에 토큰을 서버에 전송하거나 필요한 작업을 수행
        val msg = getString(R.string.msg_token_fmt, token)
        CHLog.d(TAG, msg)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // FCM 메시지 처리 로직
        CHLog.d(TAG, "time: ${remoteMessage.collapseKey}")
        CHLog.d(TAG, "Notification Message Body: ${remoteMessage.data}")

        remoteMessage.data.let { body ->
            CHLog.d(TAG, "Notification Message Body: $body")

            try {

                // json형식에서 optString을 활용해 데이터 변수로 꺼내오는 작업

//                val jsonObject = body
//                val dataOJ = jsonObject.getJSONObject("data")
//
//                CHLog.d("jsonDT", "JSONDT: $dataOJ")
//
//                val id = dataOJ.optString("id")
//                val detail = dataOJ.optString("detail")
//                val name = dataOJ.optString("name")
//                val type = dataOJ.optString("type")

                val id = body["id"]
                val detail = body["detail"]
                val name = body["name"]
                val type = body["type"]

                CHLog.d("jsonData", "ID: $id, Detail: $detail, Name: $name, Type: $type")
                    //apply는 this 키워드 생략후 접근 가능, 객체 초기화 시 유용
                val intent = Intent("newMessage").apply {
                    `package` = "com.example.project2"
                    putExtra("id", id)
                    putExtra("detail", detail)
                    putExtra("name", name)
                    putExtra("type", type)
                }

                sendBroadcast(intent)

            } catch (e: Exception) {
                Log.e(TAG, "Error parsing JSON", e)
            }
        }




    //메세지를 수신시 브로드 캐스트 생성
//        remoteMessage.notification?.body?.let { messageBody ->
//            val intent = Intent()
//            intent.`package` = "com.example.project2"
//            intent.action = "newMessage"
//            intent.putExtra("message", messageBody)
//            //생성한 브로드 캐스트 인텐트를 시스템에 송신
//
//            sendBroadcast(intent)
//            var intentMess = intent?.getStringExtra("message")
//            CHLog.d("loglog","$intentMess")
//        }
    }
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//        // FCM 메시지 처리 로직
//        CHLog.d(TAG, "From: ${remoteMessage.from}")
//        CHLog.d(TAG, "Notification Message Body: ${remoteMessage.notification?.body}")
////        CHLog.d("","Notification Message time: ${remoteMessage.priority}")
////        val jsonData= intent?.getStringExtra("json_data")
//
//
//        //메세지를 수신시 브로드 캐스트 생성
//        remoteMessage.notification?.body?.let { messageBody ->
//            val intent = Intent()
//            intent.`package` = "com.example.project2"
//            intent.action = "newMessage"
//            intent.putExtra("message", messageBody)
//            //생성한 브로드 캐스트 인텐트를 시스템에 송신
//
//            sendBroadcast(intent)
//            var intentMess = intent?.getStringExtra("message")
//            CHLog.d("loglog","$intentMess")
//        }
//    }
}

