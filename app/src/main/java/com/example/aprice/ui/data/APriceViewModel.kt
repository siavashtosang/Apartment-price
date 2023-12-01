package com.example.aprice.ui.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class APriceViewModel @Inject constructor(private val repository: ApriceRepository) : ViewModel() {
    var itemsState by mutableStateOf(Items())
    var settingsState by  mutableStateOf(SettingItems())
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

    fun lessElevator(newValue: String) {
        calculateState = calculateState.copy(lessElevator = newValue)
    }

    fun lessGarage(newValue: String) {
        calculateState = calculateState.copy(lessGarage = newValue)
    }

    fun lessLoan(newValue: String) {
        calculateState = calculateState.copy(lessLoan = newValue)
    }

    fun confirmLoanYear(newValue: String) {
        calculateState = calculateState.copy(confirmLoanYear = newValue)
    }

    fun balancePercent(newValue: String) {
        calculateState = calculateState.copy(balancePercent = newValue)
    }

    fun apartmentPrice(items: Items = itemsState, calculate: Calculate = calculateState) {
        val apartmentPrice = items.apartmentPrice.toLong()
        val firstStep =
            ((apartmentPrice * (calculate.firstPercent.toDouble() / 100)) * (items.currentDate - items.apartmentAge)).toLong()
        var finalPrice = apartmentPrice - firstStep
        if (!items.elevator) {
            val floor =
                ((items.floor.toInt() - 1) * (calculate.lessElevator.toDouble() / 100)) * finalPrice
            finalPrice = (finalPrice - floor).toLong()
        }
        if (!items.garage) {
            val garage = finalPrice * (calculate.lessGarage.toDouble() / 100)
            finalPrice = (finalPrice - garage).toLong()
        }
        if ((items.currentDate - items.apartmentAge) >= calculate.confirmLoanYear.toInt()) {
            items.loan = false
            val loan = finalPrice * (calculate.lessLoan.toDouble() / 100)
            finalPrice = (finalPrice - loan).toLong()
        } else {
            items.loan = true
        }
        if (finalPrice <= 0) {
            finalPrice = 0
        }
        if (finalPrice >= items.apartmentPrice.toLong()) {
            finalPrice = items.apartmentPrice.toLong()
        }
        items.finalPrice = finalPrice
        val balancePercent = (finalPrice * (calculate.balancePercent.toDouble() / 100))
        var tenPercentageIncrease = (finalPrice + balancePercent).toLong()
        if (tenPercentageIncrease >= items.apartmentPrice.toLong()) {
            tenPercentageIncrease = items.apartmentPrice.toLong()
        }
        items.tenPercentageIncrease = tenPercentageIncrease

        items.tenPercentageDecrease = (finalPrice - balancePercent).toLong()

    }

    fun insertNewSetting(){
        viewModelScope.launch {
            repository.insertSettingItems(
                Settings(
                    lessElevator = calculateState.lessElevator,
                    lessGarage = calculateState.lessGarage,
                    lessLoan = calculateState.lessLoan,
                    balancePercent = calculateState.balancePercent,
                    confirmLoanYear = calculateState.confirmLoanYear
                )
            )
        }
    }

    fun updateNewSetting(){
        viewModelScope.launch {
            repository.updateSettingItems(
                Settings(
                    lessElevator = calculateState.lessElevator,
                    lessGarage = calculateState.lessGarage,
                    lessLoan = calculateState.lessLoan,
                    balancePercent = calculateState.balancePercent,
                    confirmLoanYear = calculateState.confirmLoanYear
                )
            )
        }
    }

    init {
        getSettings()
    }
    private fun getSettings(){
        viewModelScope.launch {
            repository.allSettingItems().collectLatest {
                settingsState = settingsState.copy(settings = it)
            }
        }
    }
}



data class SettingItems(
    val settings: Settings? = null
)
data class Items(
    var apartmentPrice: String = "",
    var apartmentAge: Int = 1402,
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
    var firstPercent: String = "1.5",
    var lessElevator: String = "3.3",
    var lessGarage: String = "12.5",
    var lessLoan: String = "20",
    var confirmLoanYear: String = "26",
    var balancePercent: String = "5",

    )