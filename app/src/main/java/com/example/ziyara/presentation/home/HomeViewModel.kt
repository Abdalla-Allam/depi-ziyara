package com.example.ziyara.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ziyara.data.local.entity.PlaceEntity
import com.example.ziyara.data.repository.PlaceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PlaceRepository,val context: Context) : ViewModel() {

    private val _places = MutableStateFlow<List<PlaceEntity>>(emptyList())
    val places: StateFlow<List<PlaceEntity>> = _places.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _filteredPlaces = MutableStateFlow<List<PlaceEntity>>(emptyList())
    val filteredPlaces: StateFlow<List<PlaceEntity>> = _filteredPlaces.asStateFlow()


    init {
        CoroutineScope(Dispatchers.IO).launch {
            repository.pepopulateDatabase(context)
        observePlaces()
    }
    }

    private fun observePlaces() {
        viewModelScope.launch {
            repository.getAllPlaces().collect { allPlaces ->
                _places.value = allPlaces
                filterPlaces(_selectedCategory.value, allPlaces)
            }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        filterPlaces(category, _places.value)
    }

    private fun filterPlaces(category: String, allPlaces: List<PlaceEntity>) {
        _filteredPlaces.value = if (category == "All") {
            allPlaces
        } else {
            allPlaces.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    fun toggleFavorite(place: PlaceEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(place.id, !place.isFavorite)
        }
    }
}