package com.example.aprice.ui.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [Settings::class], version = 1, exportSchema = false)
abstract class ApriceDataBase : RoomDatabase() {

    abstract fun settingDao(): SettingDao

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext appContext: Context): ApriceDataBase {
            return Room.databaseBuilder(
                appContext,
                ApriceDataBase::class.java,
                "room_database"
            ).build()
        }

        @Provides
        @Singleton
        fun provideSettingsDao(appDatabase: ApriceDataBase): SettingDao {
            return appDatabase.settingDao()
        }

        @Provides
        @Singleton
        fun provideTaskRepository(
            settingDao: SettingDao
        ): ApriceRepository {
            return OfflineApriceRepository(settingDao)
        }
    }

}