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

package com.google.samples.apps.sunflower.mvpdemo

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.samples.apps.sunflower.data.PlantRepository
import io.reactivex.disposables.CompositeDisposable
import kotlin.random.Random

class PlantListPresenterImpl(
        private val plantRepository: PlantRepository)
    : PlantListContract.Presenter
//        ,LifecycleObserver
{

    private var view: PlantListContract.View? = null
    private val disposeBag = CompositeDisposable()

    override fun subscribe(view: PlantListContract.View) {
        this.view = view
//        /**
//         * for lifecycle awareness
//         * */
//        (this.view as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun unSubscribe() {
        disposeBag.clear()
        view = null
    }

    override fun getPlant(growZone: Int) {
        if (growZone == PlantListFragmentMVP.NO_GROW_ZONE) {
            disposeBag.add(plantRepository.getListPlants().subscribe { plantData ->
                view?.updatePlantAdapter(plantData)
            })
        } else {
            disposeBag.add(plantRepository.getPlantsListWithGrowZoneNumber(growZone).subscribe { plantData ->
                view?.updatePlantAdapter(plantData)
            })
        }
    }

    override fun filtered(growZone: Int) {
        if (growZone != PlantListFragmentMVP.NO_GROW_ZONE) {
            view?.updateGrowZoneNumber(PlantListFragmentMVP.NO_GROW_ZONE)
        } else {
            view?.updateGrowZoneNumber(Random.nextInt(1, 10))
        }
    }


}