package com.example.ziyara.presentation

import com.example.ziyara.data.local.entity.PlaceEntity

sealed class PlaceUiState {
    object Loading : PlaceUiState()
    data class Success(val places: List<PlaceEntity>) : PlaceUiState()
    data class Error(val message: String) : PlaceUiState()
}