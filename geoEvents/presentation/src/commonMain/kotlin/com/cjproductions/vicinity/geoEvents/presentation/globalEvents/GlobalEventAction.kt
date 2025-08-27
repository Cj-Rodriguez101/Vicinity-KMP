package com.cjproductions.vicinity.geoEvents.presentation.globalEvents

sealed interface GlobalEventAction {
    data object OnCompassClick: GlobalEventAction
}