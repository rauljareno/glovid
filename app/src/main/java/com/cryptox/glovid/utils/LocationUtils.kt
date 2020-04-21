package com.cryptox.glovid.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.*


class LocationUtils {
    companion object {
        @Throws(IOException::class)
        fun getPostalCodeByCoordinates(context: Context?, lat: Double, lon: Double): String? {
            val mGeocoder = Geocoder(context, Locale.getDefault())
            var zipcode: String? = null
            var address: Address? = null
            val addresses: List<Address>? = mGeocoder.getFromLocation(lat, lon, 5)
            if (addresses != null && addresses.isNotEmpty()) {
                for (i in addresses.indices) {
                    address = addresses[i]
                    if (address.postalCode != null) {
                        zipcode = address.postalCode
                        break
                    }
                }
                return zipcode
            }
            return null
        }
    }
}