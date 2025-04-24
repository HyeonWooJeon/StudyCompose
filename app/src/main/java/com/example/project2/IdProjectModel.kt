package com.example.project2

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class IdProjectModel() : ViewModel() {

    var bottomSheet by mutableStateOf(false)
    //전체 상태 체크
    var checkedState by mutableStateOf(false)
    //개별 상태 체크
    var listCheckState by mutableStateOf(List(4) { false })

    fun updateCheckedState(checked: Boolean) {
        checkedState = checked
        listCheckState = List(listCheckState.size) { checked }
    }

    //bottomSheet 상태 false
    fun onBottomSheetDismiss() {
        bottomSheet = false
    }

    //bottomSheet 상태 true
    fun onBottomSheetClick() {
        bottomSheet = true
    }
}
