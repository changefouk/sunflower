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

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.adapters.PlantAdapter
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.databinding.FragmentPlantListBinding
import org.koin.android.ext.android.inject

class PlantListFragmentMVP : Fragment(), PlantListContract.View, LifecycleOwner {

    private var binding: FragmentPlantListBinding? = null
    private var growZone = NO_GROW_ZONE
    private val adapter = PlantAdapter()

    private val presenter: PlantListContract.Presenter by inject()

    companion object {
        const val NO_GROW_ZONE = -1
        private const val GROW_ZONE_SAVED_STATE_KEY = "GROW_ZONE_SAVED_STATE_KEY"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentPlantListBinding: FragmentPlantListBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_plant_list, container, false)

        context ?: return fragmentPlantListBinding.root
        fragmentPlantListBinding.plantList.adapter = adapter

        savedInstanceState?.let {
            growZone = it.getInt(GROW_ZONE_SAVED_STATE_KEY, NO_GROW_ZONE)
        }

        presenter.subscribe(this)
        presenter.getPlant(growZone)

        setHasOptionsMenu(true)

        this.binding = fragmentPlantListBinding
        return fragmentPlantListBinding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(GROW_ZONE_SAVED_STATE_KEY, growZone)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_plant_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_zone -> {
                presenter.filtered(growZone)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun updatePlantAdapter(plantList: List<Plant>) {
        binding?.hasPlantList = !plantList.isNullOrEmpty()
        adapter.submitList(plantList)
    }

    override fun updateGrowZoneNumber(growZone: Int) {
        this.growZone = growZone
        presenter.getPlant(growZone)
    }

}