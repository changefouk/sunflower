/*
 * Copyright 2018 Google LLC
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

package com.google.samples.apps.sunflower.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Repository module for handling data operations.
 */
class PlantRepository constructor(private val plantDao: PlantDao) {

    fun getPlants() = plantDao.getPlants()

    fun getPlant(plantId: String) = plantDao.getPlant(plantId)

    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int) =
            plantDao.getPlantsWithGrowZoneNumber(growZoneNumber)

    fun getListPlants(): Single<List<Plant>> =
            plantDao.getListPlants()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    fun getPlantsListWithGrowZoneNumber(growZoneNumber: Int): Single<List<Plant>> =
            plantDao.getListPlantsWithGrowZoneNumber(growZoneNumber)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


//    companion object {
//
//        // For Singleton instantiation
//        @Volatile private var instance: PlantRepository? = null
//
//        fun getInstance(plantDao: PlantDao) =
//                instance ?: synchronized(this) {
//                    instance ?: PlantRepository(plantDao).also { instance = it }
//                }
//    }
}