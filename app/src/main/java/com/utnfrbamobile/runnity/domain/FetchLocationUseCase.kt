package com.utnfrbamobile.runnity.domain

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CompletableDeferred
import java.lang.RuntimeException

class FetchLocationUseCase(private val locationClient : FusedLocationProviderClient) {

    @SuppressLint("MissingPermission")
    suspend operator fun invoke() : Location {
        val deferred = CompletableDeferred<Location>()

        locationClient.lastLocation.addOnSuccessListener {
            deferred.complete(it)
        }.addOnCanceledListener {
            deferred.completeExceptionally(RuntimeException())
        }
        return deferred.await()
    }
}