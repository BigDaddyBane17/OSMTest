package com.example.mapdetails

sealed interface MapDetailsEffect {
    data class ShowMessage(val text: String) : MapDetailsEffect
}