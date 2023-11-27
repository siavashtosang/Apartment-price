package com.example.aprice.ui.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class APriceViewModel @Inject constructor() : ViewModel() {
    var itemsState by mutableStateOf(Items())
    var calculateState by mutableStateOf(Calculate())
        private set

    fun onApartmentPrice(newValue: String) {
        itemsState = itemsState.copy(apartmentPrice = newValue)
    }

    fun onApartmentAge(newValue: Int) {
        itemsState = itemsState.copy(apartmentAge = newValue)
    }

    fun onFloor(newValue: String) {
        itemsState = itemsState.copy(floor = newValue)
    }

    fun onGarage(newValue: Boolean) {
        itemsState = itemsState.copy(garage = newValue)
    }

    fun onElevator(newValue: Boolean) {
        itemsState = itemsState.copy(elevator = newValue)
    }

    fun apartmentPrice(items: Items = itemsState, calculate: Calculate = calculateState) {
        val apartmentPrice = items.apartmentPrice.toLong()
        val firstStep =
            ((apartmentPrice * calculate.firstPercent) * (items.currentDate - items.apartmentAge)).toLong()
        var finalPrice = apartmentPrice - firstStep
        if (!items.elevator) {
            val floor = ((items.floor.toInt() - 1) * calculate.lessElevator) * finalPrice
            finalPrice = (finalPrice - floor).toLong()
        }
        if (!items.garage) {
            val garage = finalPrice * calculate.lessGarage
            finalPrice = (finalPrice - garage).toLong()
        }
        if ((items.currentDate - items.apartmentAge) >= 26) {
            val loan = finalPrice * calculate.lessLoan
            finalPrice = (finalPrice - loan).toLong()
        }
        items.finalPrice = finalPrice
        val balancePercent =  (finalPrice * calculate.balancePercent)
        items.tenPercentageIncrease = (finalPrice + balancePercent).toLong()
        items.tenPercentageDecrease = (finalPrice - balancePercent).toLong()

    }
}

data class Items(
    var apartmentPrice: String = "",
    var apartmentAge: Int = 0,
    var floor: String = "",
    var garage: Boolean = false,
    var elevator: Boolean = false,
    var currentDate: Int = 1402,
    var loan: Boolean = false,
    var tenPercentageIncrease: Long = 0,
    var tenPercentageDecrease: Long = 0,
    var finalPrice: Long = 0,
)

data class Calculate(
    var firstPercent: Double = 0.015,
    var lessElevator: Double = 0.033,
    var lessGarage: Double = 0.125,
    var lessLoan: Double = 0.2,
    var balancePercent: Double = 0.1,

    )