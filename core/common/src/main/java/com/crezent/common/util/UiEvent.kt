package com.crezent.common.util

sealed interface UiEvent {
    object Success: UiEvent
    object NavigateUp: UiEvent
    data class ShowSnackBar(val message: UiText): UiEvent
}

