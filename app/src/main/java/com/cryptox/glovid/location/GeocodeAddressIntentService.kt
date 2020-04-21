package com.cryptox.glovid.location

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import android.util.Log
import java.io.IOException
import java.util.*

class GeocodeAddressIntentService : IntentService("GeocodeAddressIntentService") {
    protected var resultReceiver: ResultReceiver? = null
    override fun onHandleIntent(intent: Intent?) {
        Log.e(TAG, "onHandleIntent")
        val geocoder = Geocoder(this, Locale.getDefault())
        var errorMessage = ""
        var addresses: List<Address>? = null
        val fetchType = intent!!.getIntExtra(LocationConstants.FETCH_TYPE_EXTRA, 0)
        Log.e(TAG, "fetchType == $fetchType")
        if (fetchType == LocationConstants.USE_ADDRESS_NAME) {
            val name =
                intent.getStringExtra(LocationConstants.LOCATION_NAME_DATA_EXTRA)
            try {
                addresses = geocoder.getFromLocationName(name, 1)
            } catch (e: IOException) {
                errorMessage = "Service not available"
                Log.e(TAG, errorMessage, e)
            }
        } else if (fetchType == LocationConstants.USE_ADDRESS_LOCATION) {
            val latitude =
                intent.getDoubleExtra(LocationConstants.LOCATION_LATITUDE_DATA_EXTRA, 0.0)
            val longitude =
                intent.getDoubleExtra(LocationConstants.LOCATION_LONGITUDE_DATA_EXTRA, 0.0)
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1)
            } catch (ioException: IOException) {
                errorMessage = "Service Not Available"
                Log.e(
                    TAG,
                    errorMessage,
                    ioException
                )
            } catch (illegalArgumentException: IllegalArgumentException) {
                errorMessage = "Invalid Latitude or Longitude Used"
                Log.e(
                    TAG, errorMessage + ". " +
                            "Latitude = " + latitude + ", Longitude = " +
                            longitude, illegalArgumentException
                )
            }
        } else {
            errorMessage = "Unknown Type"
            Log.e(TAG, errorMessage)
        }
        resultReceiver =
            intent.getParcelableExtra(LocationConstants.RECEIVER)
        if (addresses == null || addresses.size == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Not Found"
                Log.e(TAG, errorMessage)
            }
            deliverResultToReceiver(LocationConstants.FAILURE_RESULT, errorMessage, null)
        } else {
            for (address in addresses) {
                var outputAddress = ""
                for (i in 0 until address.maxAddressLineIndex) {
                    outputAddress += " --- " + address.getAddressLine(i)
                }
                Log.e(TAG, outputAddress)
            }
            val address = addresses[0]
            val addressFragments =
                ArrayList<String?>()
            for (i in 0 until address.maxAddressLineIndex) {
                addressFragments.add(address.getAddressLine(i))
            }
            Log.i(TAG, "Address Found")
            deliverResultToReceiver(
                LocationConstants.SUCCESS_RESULT,
                TextUtils.join(
                    System.getProperty("line.separator")!!,
                    addressFragments
                ), address
            )
        }
    }

    private fun deliverResultToReceiver(resultCode: Int, message: String, address: Address?) {
        val bundle = Bundle()
        bundle.putParcelable(LocationConstants.RESULT_ADDRESS, address)
        bundle.putString(LocationConstants.RESULT_DATA_KEY, message)
        resultReceiver!!.send(resultCode, bundle)
    }

    companion object {
        private const val TAG = "GEO_ADDRESS_SERVICE"
    }
}