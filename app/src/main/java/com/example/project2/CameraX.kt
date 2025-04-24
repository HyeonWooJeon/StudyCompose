package com.example.project2

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

data class RecordingInfo(
    val duration: Long,
    val filePath: String
)

// RecordingState 정의 (예제, 실제 정의는 요구 사항에 맞게 수정)
sealed class RecordingState {
    object Recording : RecordingState()
    object NotRecording : RecordingState()
}

interface CameraX {
    fun initialize(context: Context)
    fun startCamera(lifecycleOwner: LifecycleOwner)
    // 이미지 캡쳐
    fun takePicture(onImageCaptured: (Bitmap?) -> Unit)
    //  fun takePicture(onImageCaptured: (Bitmap?, Uri?) -> Unit)
    // 캡쳐 이미지 디렉토리 저장
    fun saveImageToMediaStore(bitmap: Bitmap):Uri?
    fun startRecordVideo()
    fun stopRecordVideo()
    fun resumeRecordVideo()
    fun pauseRecordVideo()
    fun closeRecordVideo()
    fun flipCameraFacing()
    fun turnOnOffFlash()
    fun unBindCamera()
    fun getPreviewView() : PreviewView
    fun getFlashState() : StateFlow<Boolean>
    fun getFacingState() : StateFlow<Int>
    fun getRecordingState() : StateFlow<RecordingState>
    fun getRecordingInfo() : SharedFlow<RecordingInfo>
    fun shutdownExecutor()
}