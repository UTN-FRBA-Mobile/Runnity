package com.utnfrbamobile.runnity.domain

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.rembertime.location.factory.LocationUseCaseProvider
import com.rembertime.location.strategy.PriorityType
import com.rembertime.location.strategy.RetryType

class FetchLocationUseCase(private val applicationContext: Context) {

    private val locationProvider = LocationUseCaseProvider.Builder(applicationContext)
        .withTimeOutPerAttemptInMillis(200)
        .withRetryDelayInMillis(150)
        .withAttempts(5)
        .withRetryStrategy(RetryType.EXPONENTIAL_BACK_OFF)
        .withFastestIntervalReceivingInMillis(10_000)
        .withIntervalReceivingInMillis(10_000)
        .withRequestPriority(PriorityType.PRIORITY_HIGH_ACCURACY)
        .build()

    @SuppressLint("MissingPermission")
    suspend operator fun invoke() : Location? {
        return locationProvider()
    }
}