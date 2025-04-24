package com.example.project2

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.InterruptedIOException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


internal class CameraXImpl : CameraX {
    private lateinit var context: Context
    private lateinit var previewView: PreviewView
    private lateinit var provider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var cameraProvider: ListenableFuture<ProcessCameraProvider>
    private lateinit var camera: Camera
    private lateinit var imageCapture : ImageCapture
    private lateinit var executor : ExecutorService
    private lateinit var videoCapture: VideoCapture<Recorder>
    private lateinit var mediaStoreOutput: MediaStoreOutputOptions

    private val _facing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    private val flashState = MutableStateFlow(false)
    private val facingState = MutableStateFlow(0)
    private val recordingState = MutableStateFlow(RecordingState.NotRecording)
    private val recordingInfo = MutableSharedFlow<RecordingInfo>()

    override fun initialize(context: Context) {
        //카메라 초기화
        this.context = context
        previewView = PreviewView(context)
        preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }
        cameraProvider = ProcessCameraProvider.getInstance(context)
        cameraProvider.addListener({
            provider = cameraProvider.get()
        }, ContextCompat.getMainExecutor(context))
        imageCapture = ImageCapture.Builder().build()
        executor = Executors.newSingleThreadExecutor()
        initializeVideo()
    }

    @OptIn(ExperimentalCamera2Interop::class)
    fun initializeVideo(){
        val qualitySelector = QualitySelector.fromOrderedList(
            listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
            FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
        )

        val recorder = Recorder.Builder()
            .setExecutor(executor)
            .setQualitySelector(qualitySelector)
            .build()
        videoCapture = VideoCapture.withOutput(recorder)

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/cameraX")
        if (!path.exists()) path.mkdirs()
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA).format(System.currentTimeMillis()) + ".mp4"

        mediaStoreOutput = MediaStoreOutputOptions.Builder(context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, name)
            })
            .build()
    }

    override fun startCamera(lifecycleOwner: LifecycleOwner) {
        // 카메라 시작 로직
        val cameraSelector = CameraSelector.Builder().requireLensFacing(_facing.value).build()
        cameraProvider.addListener({
            try {
                provider.unbindAll()
                camera = provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    videoCapture
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

//    override fun takePicture(onImageCaptured: (Bitmap?, Uri?) -> Unit) {
//        val contentResolver = context.contentResolver
//        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA)
//            .format(System.currentTimeMillis()) + ".jpg"
//
////        val contentValues = ContentValues().apply {
////            put(MediaStore.Images.Media.DISPLAY_NAME, name)
////            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
////            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/cameraX")
////        }
//        CHLog.d("CameraXImpl", " val contentValues = ContentValues().apply 전까지")
//
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, name)
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/cameraX")
//            put(MediaStore.Images.Media.IS_PENDING, 1) // 이미지가 임시 상태임을 표시
//        }
//
////       val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues) invaild content error 가 뜨면서 문제가 생겨 stackoverflow에서 찾은 코드로 수정
//        val uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
//
//
//        CHLog.d("CameraXImpl", "uri != null 전까지")
//        CHLog.d("CameraXUri", "$uri")
//
//        if (uri != null) {
//            CHLog.d("CameraXImpl", "uri != not null 내부")
//            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(contentResolver, uri, contentValues).build()
//
//            imageCapture.takePicture(
//                outputFileOptions,
//                ContextCompat.getMainExecutor(context),
//                object : ImageCapture.OnImageSavedCallback {
//                    override fun onError(error: ImageCaptureException) {
//                        error.printStackTrace()
//                        onImageCaptured(null,null)
//                    }
//
//                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                        val savedUri = outputFileResults.savedUri
//                        CHLog.d("CameraXImpl", "Image saved at: $savedUri")
//
//                        contentValues.clear()
//                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
//                        contentResolver.update(savedUri!!, contentValues, null, null)
//
//                        val bitmap = savedUri?.let { uri ->
//                            contentResolver.openInputStream(uri)?.use {
//                                BitmapFactory.decodeStream(it)
//                            }
//                        }
//
//                        onImageCaptured(bitmap, savedUri)
//                    }
//                }
//            )
//        } else {
//            onImageCaptured(null, null)
//        }
//    }
//
    // CameraXImpl
    override fun takePicture(onImageCaptured: (Bitmap?) -> Unit) {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = imageProxyToBitmap(image)
                    image.close()
                    onImageCaptured(bitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    onImageCaptured(null)
                }
            }
        )
    }

    override fun saveImageToMediaStore(bitmap: Bitmap): Uri?{
        val contentResolver = context.contentResolver
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA)
            .format(System.currentTimeMillis()) + ".jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/cameraX")
        }

//        val uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if(uri != null) {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)
        }
        return uri
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
    }



    override fun shutdownExecutor() {
        if (!executor.isShutdown) {
            executor.shutdown()
            try{
                if(!executor.awaitTermination(60, TimeUnit.SECONDS)){
                    executor.shutdownNow()
                }
            }catch (e: InterruptedIOException){
                executor.shutdownNow()
            }
        }
    }
    override fun startRecordVideo() {
        // 비디오 녹화 시작 로직
    }

    override fun stopRecordVideo() {
        // 비디오 녹화 중지 로직
    }

    override fun resumeRecordVideo() {
        // 비디오 녹화 재개 로직
    }

    override fun pauseRecordVideo() {
        // 비디오 녹화 일시정지 로직
    }

    override fun closeRecordVideo() {
        // 비디오 녹화 종료 로직
    }

    override fun flipCameraFacing() {
        // 카메라 방향 전환 로직
    }

    override fun turnOnOffFlash() {
        // 플래시 켜기/끄기 로직
    }

    override fun unBindCamera() {
        // 카메라 언바인드 로직
        provider.unbindAll()
    }

    override fun getPreviewView(): PreviewView = previewView
    override fun getFlashState(): StateFlow<Boolean> = flashState
    override fun getFacingState(): StateFlow<Int> = facingState
    override fun getRecordingState(): StateFlow<RecordingState> = recordingState
    override fun getRecordingInfo(): SharedFlow<RecordingInfo> = recordingInfo
}
