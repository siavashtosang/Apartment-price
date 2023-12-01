package com.example.aprice.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val id: Int = 0,
    var lessElevator: String,
    var lessGarage: String,
    var lessLoan: String,
    var confirmLoanYear: String,
    var balancePercent: String,
)