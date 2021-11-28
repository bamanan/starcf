package fr.istic.coulibaly.fazul.horairesbus.api.utils

import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.*
import java.io.File
import java.nio.file.Files


class TextFileToEntity(private val fileName: String) {

    private val list: MutableList<Any> = mutableListOf()

    fun entities(): List<Any> {
        val file = File(fileName)
        val fileEntries = getFileEntries(file)

        when (file.name.removeSuffix(".txt")) {
            StarContract.CALENDAR -> {
                fileEntries.rows.forEach { entry ->
                    val calendar = Calendar(
                        startDate = entry[StarContract.Calendar.CalendarColumns.START_DATE]!!,
                        endDate = entry[StarContract.Calendar.CalendarColumns.END_DATE]!!,
                        monday = entry[StarContract.Calendar.CalendarColumns.MONDAY]!!,
                        tuesday = entry[StarContract.Calendar.CalendarColumns.TUESDAY]!!,
                        wednesday = entry[StarContract.Calendar.CalendarColumns.WEDNESDAY]!!,
                        thursday = entry[StarContract.Calendar.CalendarColumns.THURSDAY]!!,
                        friday = entry[StarContract.Calendar.CalendarColumns.FRIDAY]!!,
                        saturday = entry[StarContract.Calendar.CalendarColumns.SATURDAY]!!,
                        sunday = entry[StarContract.Calendar.CalendarColumns.SUNDAY]!!,
                    )
                    list.add(calendar)
                }
            }

            StarContract.ROUTES -> {
                fileEntries.rows.forEach { entry ->
                    val busRoute = BusRoute(
                        shortName = entry[StarContract.BusRoutes.BusRouteColumns.SHORT_NAME]!!,
                        longName = entry[StarContract.BusRoutes.BusRouteColumns.LONG_NAME]!!,
                        description = entry[StarContract.BusRoutes.BusRouteColumns.DESCRIPTION]!!,
                        type = entry[StarContract.BusRoutes.BusRouteColumns.TYPE]!!,
                        textColor = entry[StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR]!!,
                        color = entry[StarContract.BusRoutes.BusRouteColumns.COLOR]!!
                    )
                    list.add(busRoute)
                }
            }
            StarContract.STOPS -> {
                fileEntries.rows.forEach { entry ->
                    val stop = Stop(
                        name = entry[StarContract.Stops.StopColumns.NAME]!!,
                        description = entry[StarContract.Stops.StopColumns.DESCRIPTION]!!,
                        latitude = entry[StarContract.Stops.StopColumns.LATITUDE]!!,
                        longitude = entry[StarContract.Stops.StopColumns.LONGITUDE]!!,
                        wheelchairBoarding = entry[StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING]!!
                    )
                    list.add(stop)
                }

            }
            StarContract.STOPTIMES -> {
                fileEntries.rows.forEach { entry ->
                    val stopTime = StopTime(
                        tripId = entry[StarContract.StopTimes.StopTimeColumns.TRIP_ID]!!,
                        stopId = entry[StarContract.StopTimes.StopTimeColumns.STOP_ID]!!,
                        arrivalTime = entry[StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME]!!,
                        departureTime = entry[StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME]!!,
                        stopSequence = entry[StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE]!!,
                    )
                    list.add(stopTime)
                }

            }
            StarContract.TRIPS -> {
                fileEntries.rows.forEach { entry ->
                    val trip = Trip(
                        blockId = entry[StarContract.Trips.TripColumns.BLOCK_ID]!!,
                        directionId = entry[StarContract.Trips.TripColumns.DIRECTION_ID]!!,
                        routeId = entry[StarContract.Trips.TripColumns.ROUTE_ID]!!,
                        serviceId = entry[StarContract.Trips.TripColumns.SERVICE_ID]!!,
                        tripHeadSign = entry[StarContract.Trips.TripColumns.HEADSIGN]!!,
                        wheelchairAccessible = entry[StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE]!!,
                    )
                    list.add(trip)
                }

            }
        }
        return list
    }


    private fun getFileEntries(file: File): FileEntries {
        var firstLine = true
        var head = listOf<String>()
        val row = mutableMapOf<String, String>()
        val fileEntries = mutableListOf<Map<String, String>>()
        file.forEachLine {
            if (firstLine) {
                head = it.split(",")
                firstLine = false
            } else {
                val line = it.split(",")
                for (c in line.indices) {
                    row[head[c]] = line[c].removeSurrounding("\"")
                }
                fileEntries.add(row)
            }
        }
        return FileEntries(fileEntries)
    }
}
