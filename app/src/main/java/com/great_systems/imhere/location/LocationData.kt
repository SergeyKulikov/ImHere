package com.great_systems.imhere.location

import android.location.Address
import android.location.Location
import com.google.android.gms.location.LocationResult

class LocationData (
    val locationResult: Location,
    val listAddress: List<Address>? = null,
    val firstAddress: String
)
