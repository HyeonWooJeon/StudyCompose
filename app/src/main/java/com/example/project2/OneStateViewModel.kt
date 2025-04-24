package com.example.project2

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneStateViewModel @Inject constructor() : BaseViewModel<MainState, MainAction>() {
    override fun processAction(action: MainAction) {
        when (action) {
            MainAction.ContinueData -> {
                viewModelScope.launch {
                    var counter = 0
                    while (true) {
                        counter = ++counter

                        if (counter % 2 == 0) {
                            setState(
                                getValue().copy(
                                    playerAData = getValue().playerAData.copy(
                                        text = "A-${counter}",
                                        list = getValue().playerAData.list + counter
                                    )
//                                    playerAText = "A-${counter}",
//                                    listA = getValue().listA + counter
                                )
                            )
                        }

                        if (counter % 3 == 0) {
                            setState(
                                getValue().copy(
                                     playerBData = getValue().playerBData.copy(
                                         text = "B-${counter}",
                                         list = getValue().playerAData.list + counter
                                     )
//                                    playerBText = "B-${counter}",
//                                    listB = getValue().listB + counter
                                )
                            )
                        }

                        if (counter % 5 == 0) {
                            setState(
                                getValue().copy(
                                    playerCData = getValue().playerCData.copy(
                                        text = "C-${counter}",
                                        list = getValue().playerCData.list + counter
                                    )
//                                    playerCText = "C-${counter}",
//                                    listC = getValue().listC + counter
                                )
                            )
                        }

                        setState(
                            getValue().copy(
                                text = counter.toString()
                            )
                        )

                        delay(1000)
                    }

                }

            }

        }
    }

    override fun initialState(): MainState =
//        MainState("", "A-0", "B-0", "C-0", true, emptyList(), emptyList(), emptyList())
        MainState("",
        PlayerAData.initialData(),
        PlayerBData.initialData(),
        PlayerCData.initialData()
        )

}

data class MainState(
    val text: String,
//    val playerAText: String,
//    val playerBText: String,
//    val playerCText: String,
//    val shouldVisible: Boolean,
//    val listA: List<Int>,
//    val listB: List<Int>,
//    val listC: List<Int>
    val playerAData : PlayerAData,
    val playerBData : PlayerBData,
    val playerCData : PlayerCData
)

data class PlayerAData(
    val text: String,
    val list: List<Int>
){
    companion object {
        fun initialData() = PlayerAData("A-0", emptyList())
    }
}

data class PlayerBData(
    val text: String,
    val list: List<Int>
){
    companion object {
        fun initialData() = PlayerBData("B-0", emptyList())
    }
}

data class PlayerCData(
    val text: String,
    val list: List<Int>
){
    companion object {
        fun initialData() = PlayerCData("C-0", emptyList())
    }
}

sealed interface MainAction {
    object ContinueData : MainAction
}