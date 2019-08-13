package br.com.philippesis.geoloc17.data.models

data class LocationModel(
        var id: Int = 0,
        var latitude: Double,
        var longitude: Double,
        var dataTimeMillis: Long)