/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.di

import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.data.GardenPlantingRepository
import com.google.samples.apps.sunflower.data.PlantRepository
import com.google.samples.apps.sunflower.utilities.DATABASE_NAME
import com.google.samples.apps.sunflower.viewmodels.GardenPlantingListViewModel
import com.google.samples.apps.sunflower.viewmodels.GardenPlantingListViewModelFactory
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(get()).enqueue(request)
                    }
                })
                .build()
    }

    single { GardenPlantingRepository(get<AppDatabase>().gardenPlantingDao()) }

    single { PlantRepository(get<AppDatabase>().plantDao()) }

    factory { GardenPlantingListViewModelFactory(get()) }

}

val viewModelModule = module {
    viewModel { GardenPlantingListViewModel(get()) }
    viewModel { (plantId: String) -> PlantDetailViewModel(get(), get(), plantId) }
    viewModel { (handle: SavedStateHandle) -> PlantListViewModel(get(), handle) }
}