package com.example.project2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// BaseViewModel을 일반화된 제네릭 클래스로 정의
abstract class BaseViewModel<S, A> : ViewModel() {
    // 상태를 관리하기 위한 MutableStateFlow
    private val _state = MutableStateFlow(initialState())
    val state: StateFlow<S> get() = _state // 상태를 외부에 노출

    // 상태를 업데이트하는 메서드
    protected fun setState(newState: S) {
        _state.value = newState
    }

    // 현재 상태를 가져오는 메서드
    protected fun getValue(): S {
        return _state.value
    }

    // 액션을 처리하는 추상 메서드
    abstract fun processAction(action: A)

    // 초기 상태를 정의하는 추상 메서드
    protected abstract fun initialState(): S
}
