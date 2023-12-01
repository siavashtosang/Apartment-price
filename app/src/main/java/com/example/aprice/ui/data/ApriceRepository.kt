package com.example.aprice.ui.data

import kotlinx.coroutines.flow.Flow

interface ApriceRepository {

    fun allSettingItems(): Flow<Settings>
    suspend fun insertSettingItems(items: Settings)
    suspend fun updateSettingItems(items: Settings)
    suspend fun deleteSettingItems(items: Settings)
}
