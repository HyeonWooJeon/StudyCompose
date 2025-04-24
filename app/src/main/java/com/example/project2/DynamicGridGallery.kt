package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.math.max

class DynamicGridGallery : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val images = listOf(
                R.drawable.dog_1,
                R.drawable.dog_2,
                R.drawable.dog_3,
                R.drawable.dog_4,
                R.drawable.dog_5,
                R.drawable.dog_6,
                R.drawable.dog_7,
//                R.drawable.dog_8,
//                R.drawable.dog_9,
//                R.drawable.dog_10,
//                R.drawable.dog_11,
            )
//            RandomSizeGallery(images)
            Grid(columns = 3) {
                images.forEachIndexed { index,imageResId ->
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = when (index) {
                            0 -> Modifier
                                .span(2, 1)
                                .background(color = Color.White) // 첫 번째 이미지 (2x1)
                            1 -> Modifier
                                .span(1, 1)
                                .background(color = Color.Black)
                            2 -> Modifier
                                .span(1, 2)
                                .background(color = colorResource(id = R.color.bl_30))
                            3 -> Modifier
                                .span(2, 2)
                                .background(color = Color.Green) // 네 번째 이미지 (2x2)
                            4 -> Modifier
                                .span(3, 1)
                                .background(color = Color.Cyan)
                            5 ->
                                Modifier
                                    .span(2, 2)
                                    .background(color = Color.DarkGray)
                            6 -> Modifier.span(1,2)
                                .background(color= colorResource(id = R.color.gg_70))
                            8 ->
                                Modifier
                                    .span(3, 2)
                                    .background(color = Color.Magenta)
                            9 ->
                                Modifier
                                    .span(2, 1)
                                    .background(color = Color.Red)
                            10->
                                Modifier
                                    .span(1, 1)
                                    .background(color = Color.Yellow)
                            else -> Modifier.span(1, 1) // 기본 1x1
                        }
                    )
                }
            }
        }
    }

    interface GridScope {
        @Stable
        fun Modifier.span(columns: Int = 1, rows: Int = 1) = this.then(
            GridData(columns, rows)
        )

        companion object : GridScope
    }

    private class GridData(
        val columnSpan: Int,
        val rowSpan: Int,
    ) : ParentDataModifier {

        override fun Density.modifyParentData(parentData: Any?): Any = this@GridData

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as GridData

            if (columnSpan != other.columnSpan) return false
            if (rowSpan != other.rowSpan) return false

            return true
        }

        override fun hashCode(): Int {
            var result = columnSpan
            result = 31 * result + rowSpan
            return result
        }
    }

    private val Measurable.gridData: GridData?
        get() = parentData as? GridData

    private val Measurable.columnSpan: Int
        get() = gridData?.columnSpan ?: 1

    private val Measurable.rowSpan: Int
        get() = gridData?.rowSpan ?: 1

    data class GridInfo(
        val numChildren: Int,
        val columnSpan: Int,
        val rowSpan: Int,
    )

    @Composable
    fun Grid(
        columns: Int,
        modifier: Modifier = Modifier,
        content: @Composable GridScope.() -> Unit,
    ) {
        check(columns > 0) { "Columns must be greater than 0" }
//        val scrollState = rememberScrollState()

        Layout(
            content = { GridScope.content() },
            modifier = modifier,
        ) { measurables, constraints ->
            // calculate how many rows we need
            val standardGrid = GridData(1, 1)
            val spans = measurables.map { measurable -> measurable.gridData ?: standardGrid }
            val gridInfo = calculateGridInfo(spans, columns)
            val rows = gridInfo.sumOf { it.rowSpan }

            // build constraints
            val baseConstraints = Constraints.fixed(
                width = constraints.maxWidth / columns,
                height = constraints.maxHeight / rows,
            )
            val cellConstraints = measurables.map { measurable ->
                val columnSpan = measurable.columnSpan
                val rowSpan = measurable.rowSpan
                Constraints.fixed(
                    width = baseConstraints.maxWidth * columnSpan,
                    height = baseConstraints.maxHeight * rowSpan
                )
            }

            // measure children
            val placeAbles = measurables.mapIndexed { index, measurable ->
                measurable.measure(cellConstraints[index])
            }

            // place children
            layout(
                width = constraints.maxWidth,
                height = constraints.maxHeight,

                ) {
                var x = 0
                var y = 0
                var childIndex = 0
                gridInfo.forEach { info ->
                    repeat(info.numChildren) { index ->
                        val placeable = placeAbles[childIndex++]
                        placeable.placeRelative(
                            x = x,
                            y = y,
                        )
                        x += placeable.width
                    }
                    x = 0
                    y += info.rowSpan * baseConstraints.maxHeight
                }
            }
        }
    }

    private fun calculateGridInfo(
        spans: List<GridData>,
        columns: Int,
    ): List<GridInfo> {
        var currentColumnSpan = 0
        var currentRowSpan = 0
        var numChildren = 0
        return buildList {
            spans.forEach { span ->
                val columnSpan = span.columnSpan.coerceAtMost(columns)
                val rowSpan = span.rowSpan
                if (currentColumnSpan + columnSpan <= columns) {
                    currentColumnSpan += columnSpan
                    currentRowSpan = max(currentRowSpan, rowSpan)
                    ++numChildren
                } else {
                    add(
                        GridInfo(
                            numChildren = numChildren,
                            columnSpan = currentColumnSpan,
                            rowSpan = currentRowSpan
                        )
                    )
                    currentColumnSpan = columnSpan
                    currentRowSpan = rowSpan
                    numChildren = 1
                }
            }
            add(
                GridInfo(
                    numChildren = numChildren,
                    columnSpan = currentColumnSpan,
                    rowSpan = currentRowSpan,
                )
            )
        }
    }
}