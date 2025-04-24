package com.example.project2

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.project2.ui.theme.Project2Theme
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers


class IdProjectCheck : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    idProjectCheck()
                }
            }
        }
    }
}

@Composable
fun idProjectCheck(modifier: Modifier = Modifier){
    Box(
        modifier= Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.wt_100))
    ){
Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
Text(text = "신분증 촬영",
    modifier = Modifier.padding(top=28.dp),
    style = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 30.sp,
    )
)
Text(text = "신분증을 아래 영역에 가득차게 맞추면\n자동으로 촬영됩니다.",
    modifier=Modifier
        .padding(top=15.dp),
    textAlign = TextAlign.Center)


    //카메라 들어올 곳
Box(modifier = Modifier.padding(top= 32.dp, bottom = 32.dp))
{
    CameraSet(showSnackBar= { message-> println("Snackbar !") } )

}
    Text(text = "유의사항")
    Box(modifier = Modifier
        .padding(top = 8.dp)
        .background(
            color = colorResource(id = R.color.cg_10),
            shape = RoundedCornerShape(16.dp)
        )
    ){
        Column(modifier=Modifier
            .padding(24.dp)) {
        Text(text = "1. 회손된 신분증은 거절될 수 있습니다.")
        Text(text = "2. 어두운 배경에서 빛이 반사되지 않도록\n촬영해 주시기 바랍니다.", modifier = Modifier.padding(top=16.dp))
        Text(text = "3. 신분증의 글자가 잘 보이게 찍어주세요.", modifier = Modifier.padding(top=16.dp))
        Text(text = "4. 화면이 흔들리지 않도록 주의하세요.", modifier = Modifier.padding(top=16.dp, bottom = 24.dp))
        }

    }
}
    }
}

@Composable
fun CameraSet(showSnackBar: (String) -> Unit = {}){
    // 카메라 권한 알림
    var cameraPermissionState by remember { mutableStateOf(false) }
    // 알림 권한 알림
    var alertPermissionState by remember { mutableStateOf(false) }
    // 현재 페이지의 권한
    val context = LocalContext.current

    //알람 권한 동작에 대한 로직
    val alertPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            alertPermissionState = isGranted
            if(!isGranted){
                Toast.makeText(context, "알람 권한을 승인해주세요",Toast.LENGTH_LONG).show()
            }
        }
    )

    //카메라 권한 동작에 대한 로직
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted ->
            cameraPermissionState = isGranted
    if(!isGranted) {
        Toast.makeText(context, "카메라 권한을 승인해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        //SDK 버전체크후 런처실행
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            alertPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            alertPermissionState = true
        }
        //카메라 런처 실행
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
}

    Surface(color = MaterialTheme.colorScheme.background) {
        // 카메라 권한과 알람 권한 상태를 체크해서 true일시 CameraScreen을 띄움
        if (cameraPermissionState && alertPermissionState) {
            CameraScreen(showSnackBar)
        } else {
            RequestCameraPermissionUI()
        }
    }

}

    @Composable
    fun RequestCameraPermissionUI() {
        Text("카메라 권한을 요청중 입니다...")
    }

@Composable
fun CameraScreen(showSnackBar: (String) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraScope = rememberCoroutineScope()
    val context = LocalContext.current
    val cameraX: CameraX = remember { CameraXImpl() }
    val previewView = remember { mutableStateOf<PreviewView?>(null) }
    val capturedImage = remember { mutableStateOf<Bitmap?>(null) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        cameraX.initialize(context)
        previewView.value = cameraX.getPreviewView()
    }

    DisposableEffect(Unit) {
        cameraScope.launch(Dispatchers.Main) {
            cameraX.startCamera(lifecycleOwner)
        }
        onDispose {
            cameraX.unBindCamera()
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (capturedImage.value == null) {
            previewView.value?.let { preview ->
                AndroidView(
                    modifier = Modifier
                        .size(320.dp, 200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    factory = { preview }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

//            Button(
//                onClick = {
//                    cameraX.takePicture { bitmap, Uri ->
//                        capturedImage.value = bitmap
//                        imageUri.value = Uri
//                    }
//                },

            Button(
                onClick = {
                    cameraX.takePicture { bitmap, ->
                        capturedImage.value = bitmap
                    }
                },
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.cg_20)),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp),
                border = BorderStroke(0.5.dp, color = colorResource(id = R.color.black))
            ) {
                Text("")
            }
        } else {
            capturedImage.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(320.dp, 200.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = {
                    // 사진 저장 로직
                    capturedImage.value?.let { bitmap ->
                        val savedUri = cameraX.saveImageToMediaStore(bitmap)
                        if (savedUri != null) {
                            showSnackBar("이미지 저장 성공")
                            imageUri.value = savedUri
                        } else {
                            showSnackBar("이미지 저장 실패")
                        }
                    }
                }) {
                    Text("저장")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    capturedImage.value = null
                    imageUri.value = null
                }) {
                    Text("다시 촬영")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun idProjectCheckPreView() {
    Project2Theme {
        idProjectCheck()
    }
}