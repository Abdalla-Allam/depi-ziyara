package com.example.ziyara.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ziyara.data.local.entity.PlaceEntity
import com.example.ziyara.data.repository.PlaceRepository
import com.example.ziyara.presentation.PlaceUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PlaceRepository, val context: Context) : ViewModel() {

    private val appContext = context.applicationContext

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _featuredPlace = MutableStateFlow<PlaceEntity?>(null)
    val featuredPlace: StateFlow<PlaceEntity?> = _featuredPlace.asStateFlow()

    val places: StateFlow<List<PlaceEntity>> = repository.getAllPlaces()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredPlaces: StateFlow<List<PlaceEntity>> = combine(
        places,
        _selectedCategory,
        _searchQuery
    ) { placesList, category, query ->
        placesList.filter { place ->
            val matchesCategory = category == "All" || place.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isEmpty() || place.name.contains(query, ignoreCase = true) || place.governorate.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val uiState: StateFlow<PlaceUiState> = filteredPlaces.map { list ->
        PlaceUiState.Success(list) as PlaceUiState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlaceUiState.Loading
    )

    init {
        viewModelScope.launch {
            repository.prepopulateDatabase(appContext)
        }

        viewModelScope.launch {
            places.collectLatest { placesList ->
                if (placesList.isNotEmpty()) {
                    while (true) {
                        _featuredPlace.value = placesList.random()
                        delay(30000)
                    }
                }
            }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(place: PlaceEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(place.id, !place.isFavorite)
        }
    }

    fun clearAllFavorites() {
        viewModelScope.launch {
            repository.clearAllFavorites()
        }
    }
}