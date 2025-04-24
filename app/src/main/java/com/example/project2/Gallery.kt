package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.project2.ui.theme.Project2Theme


class Gallery: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project2Theme {
                GalleryApp()
            }
        }
    }
}

@Composable
fun GalleryApp(){
    var selectedImageItem by remember { mutableStateOf<ImageItem?>(null) }

    val ItemList = listOf(
        ImageItem(1, R.drawable.bg_moj_symbol, "법무부 로고"),
        ImageItem(2, R.drawable.moj_symbol_32, "국방부 로고"),
        ImageItem(3, R.drawable.ball__4_, "원"),
        ImageItem(4, R.drawable.img_ocr, "신분증"),
        ImageItem(5, R.drawable.ai, "AI 로고"),
        ImageItem(6, R.drawable.ic_launcher_foreground, "Android White"),
        ImageItem(7, R.drawable.pc, "컴퓨터"),
        ImageItem(8, R.drawable.mobile, "모바일 로고"),
        ImageItem(9, R.drawable.icn_arrow, "화살표")
    )

    Box(modifier = Modifier.fillMaxSize()) {
            Gallery(
                ItemList = ItemList,
                // 클릭한 이미지를 저장
                onImageClick = { clickedItem ->
                    selectedImageItem = clickedItem
                }
            )
        if (selectedImageItem != null) {

            selectedImageItem?.let { item ->
            GalleryDetail(
                ItemList = ItemList,
                ItemImageId = item.id,
                // 닫기 버튼을 누르면 다시 갤러리로 이동
                onClose = { selectedImageItem = null }
            )
        }
            BackHandler {
                selectedImageItem = null
            }
            // 갤러리 화면
        }
    }
}


//Grid
@Composable
fun Gallery(ItemList: List<ImageItem>, onImageClick: (ImageItem) -> Unit){
    Column {
        Text(text = "갤러리" , modifier = Modifier.padding(10.dp), fontWeight = FontWeight.SemiBold, fontSize = 20.sp)

        LazyVerticalGrid(
            columns = GridCells.Adaptive(96.dp),
            contentPadding = PaddingValues(12.dp),
            content = {
                items(ItemList) { item ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .border(BorderStroke(0.3.dp, Color.Gray))
                            .clickable { onImageClick(item) },
                        elevation = 0.dp,
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Column(modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                    Image(painter = painterResource(id = item.image),
                                        contentDescription = item.title,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp),
                                    contentScale = ContentScale.Inside)
                                    Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        )
    }
}

//ItemDetail
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryDetail(ItemList:List<ImageItem>, ItemImageId:Int, onClose:()->Unit){
    val pageState = rememberPagerState (
        initialPage  = ItemList.indexOfFirst { it.id == ItemImageId }
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray.copy(alpha = 0.5f))
//        .pointerInput(Unit) {
//            detectTransformGestures { _, pan, zoom, _ ->
//                scale = (scale * zoom).coerceIn(1f, 5f) // 최소 1배, 최대 5배 확대
//                offsetX += pan.x
//                offsetY += pan.y
//            }
//        },
    ) {
        HorizontalPager(
            pageCount = ItemList.size,
            state = pageState,
            modifier = Modifier.fillMaxSize()
                .padding(20.dp)

        ) { page ->

            val item = ItemList[page]

            Box(modifier = Modifier
                .fillMaxSize()
                ,
                contentAlignment = Alignment.Center){

                Image(painter = painterResource(id = item.image),
                    contentDescription = item.title,
                    modifier = Modifier
//                        .graphicsLayer(
//                        scaleX = scale,
//                        scaleY = scale,
//                        translationX = offsetX,
//                        translationY = offsetY
//                    )
                        .fillMaxSize(),
                    contentScale = ContentScale.Inside

                )
            }
        }

        Box(modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .padding(15.dp)
            .align(Alignment.TopEnd)
            .clickable(onClick = onClose),
            contentAlignment = Alignment.Center) {
            Icon(painter = painterResource(id = R.drawable.cancel), contentDescription = null, tint = Color.Black, modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun galleryPreview(){
    GalleryApp()
}

data class ImageItem(
    val id: Int,
    val image: Int,
    val title: String
)

