package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme



class DecorationBox : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            var phoneNumber by remember { mutableStateOf("") }
            Project2Theme {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 20.dp)
                ) {
                    phoneInputTextField(
                    config = PhoneNumberElement.FormatPatterns.KOR,
                    value = phoneNumber,
                    onValueChange = {newValue ->
                        phoneNumber = newValue}
                    )
                }
            }
        }
    }
}

@Composable
fun phoneInputTextField(
    modifier: Modifier = Modifier,
    config: List<PhoneNumberElement>,
    onValueChange: (String) -> Unit,
    value: String,
){
    val editableDigitCount = remember {
        config.filterIsInstance<PhoneNumberElement.EditableDigit>().size
    }
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= editableDigitCount) {
                onValueChange(newValue)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        decorationBox = {
            val digitIndexMap = remember(value) {
                var start = 0
                config.mapIndexedNotNull { index, it ->
                    if (it is PhoneNumberElement.EditableDigit) {
                        index to start++
                    } else null
                }.toMap()
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                config.forEachIndexed { index, phoneNumberElement ->
                    when (phoneNumberElement) {
                        PhoneNumberElement.EditableDigit -> {
                            digitIndexMap[index]?.let { digitIndex ->
                                EditableDigit(
                                    text = value.getOrNull(digitIndex)?.toString(),
                                )
                            }
                        }

                        is PhoneNumberElement.Mask -> {
                            Mask(
                                text = phoneNumberElement.text,
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun Mask(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 24.sp,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditableDigit(
    modifier: Modifier = Modifier,
    text: String?,
) {
    Column(
        modifier = modifier
            .width(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(
            targetState = text,
            transitionSpec = {
                (slideInVertically() + fadeIn()) with (slideOutVertically() + fadeOut())
            }) { text ->
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = text ?: " ",
                fontSize = 24.sp,
            )
        }

        Divider(
            thickness = 1.dp,
            color = Color.LightGray,
        )
    }
}


sealed interface PhoneNumberElement {
    data class Mask(val text: String) : PhoneNumberElement
    object EditableDigit : PhoneNumberElement

    object FormatPatterns {
        //RUS 번호 Form
        val RUS by lazy {
            listOf(
                Mask("+7"),
                Mask("("),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                Mask(")"),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                Mask("-"),
                EditableDigit,
                EditableDigit,
                Mask("-"),
                EditableDigit,
                EditableDigit,
            )
        }
        //KOR 번호 Form
        val KOR by lazy{
            listOf(
                Mask("+82"),
                Mask("("),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                Mask(")"),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                EditableDigit,
                Mask("-"),
                EditableDigit,
                EditableDigit,
                EditableDigit,
                EditableDigit,
            )
        }
    }
}