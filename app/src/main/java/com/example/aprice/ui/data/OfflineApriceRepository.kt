package com.example.aprice.ui.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineApriceRepository @Inject constructor(
    private val settings: SettingDao
):ApriceRepository {
    override fun allSettingItems(): Flow<Settings> = settings.getAllSettingItems()

    override suspend fun insertSettingItems(items: Settings) = settings.insert(items)

    override suspend fun updateSettingItems(items: Settings) = settings.update(items)

    override suspend fun deleteSettingItems(items: Settings) = settings.delete(items)

}