package fr.istic.mob.starcf

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.istic.mob.starcf.api.database.BusScheduleApplication
import fr.istic.mob.starcf.api.database.entity.BusRoute

class SpinnerViewModel(database: BusScheduleApplication) : ViewModel() {

    var routes = mutableListOf<BusRoute>()

    init {
        routes = database.routeRepository.routes as MutableList<BusRoute>
    }

    val routeLiveData = database.routeRepository.routesLiveData
}
class SpinnerViewModelFactory(private val database: BusScheduleApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpinnerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SpinnerViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
