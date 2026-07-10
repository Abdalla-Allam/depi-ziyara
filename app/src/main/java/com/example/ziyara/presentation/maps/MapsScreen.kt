package com.example.ziyara.presentation.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ziyara.presentation.home.HomeViewModel
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapsScreen(
    viewModel: HomeViewModel,
    initialLat: Double? = null,
    initialLng: Double? = null
) {
    val places by viewModel.places.collectAsState()

    val cairo = LatLng(30.0444, 31.2357)
    var place= LatLng(30.0444, 31.2357)
    val cameraPositionState = rememberCameraPositionState {
        if(initialLng!=null && initialLat!=null){
            place = LatLng(initialLat,initialLng)
            position = CameraPosition.fromLatLngZoom(place, 14f)
        }
        else
        position = CameraPosition.fromLatLngZoom(cairo, 11f)
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        places.forEach { place ->
            val placeLocation= LatLng(place.latitude,place.longitude)
            Marker (
                state = MarkerState(position=placeLocation),
                title = place.name,
                snippet = "${place.governorate}, ${place.category}"

            )
        }



    }
}