package fr.istic.mob.starcf.api.core.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import fr.istic.mob.starcf.api.contract.StarContract
import fr.istic.mob.starcf.api.database.BusScheduleApplication

class Provider : ContentProvider() {
    companion object {
        const val TAG = "Provider"
        private const val QUERY_ROUTES = 10
        private const val QUERY_STOPS = 20
        private const val QUERY_TRIPS = 30
        private const val QUERY_STOP_TIMES = 40
        private const val QUERY_CALENDAR = 50
        private const val QUERY_ROUTES_DETAILS = 60
        private const val QUERY_SEARCHED_STOPS = 70
        private const val QUERY_ROUTES_FOR_STOP = 80
        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)
    }

    private val database: BusScheduleApplication by lazy {
        BusScheduleApplication(context!!)
    }

    override fun onCreate(): Boolean {
        URI_MATCHER.addURI(
            StarContract.AUTHORITY,
            StarContract.BusRoutes.CONTENT_PATH,
            QUERY_ROUTES
        )
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Trips.CONTENT_PATH, QUERY_TRIPS)
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Stops.CONTENT_PATH, QUERY_STOPS)
        URI_MATCHER.addURI(
            StarContract.AUTHORITY,
            StarContract.StopTimes.CONTENT_PATH,
            QUERY_STOP_TIMES
        )
        URI_MATCHER.addURI(
            StarContract.AUTHORITY,
            StarContract.Calendar.CONTENT_PATH,
            QUERY_CALENDAR
        )
        URI_MATCHER.addURI(
            StarContract.AUTHORITY,
            StarContract.RouteDetails.CONTENT_PATH,
            QUERY_ROUTES_DETAILS
        )
        URI_MATCHER.addURI(
            StarContract.AUTHORITY,
            StarContract.SearchedStops.CONTENT_PATH,
            QUERY_SEARCHED_STOPS
        )
        URI_MATCHER.addURI(
            StarContract.AUTHORITY,
            StarContract.RoutesForStop.CONTENT_PATH,
            QUERY_ROUTES_FOR_STOP
        )

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var result: Cursor? = null
        try {
            when (URI_MATCHER.match(uri)) {
                QUERY_ROUTES -> {
                    result = database.routeRepository.routesCursor
                }
                QUERY_STOPS -> {
                    if (selectionArgs != null && selectionArgs.size > 1) {
                        val route = selectionArgs[0]
                        val direction = selectionArgs[1]
                        result = database.stopRepository.stopsByRouteAndDirectionCursor(
                            routeId = route,
                            directionId = direction
                        )
                    }
                }
                QUERY_STOP_TIMES -> {
                    if (selectionArgs != null && selectionArgs.size > 3) {
                        val route = selectionArgs[0]
                        val stop = selectionArgs[1]
                        val day = selectionArgs[3]
                        val direction = selectionArgs[2]
                        val arrival = selectionArgs[4]

                        when (day) {
                            StarContract.Calendar.CalendarColumns.MONDAY -> {
                                result =
                                    database.stopTimeRepository.stopTimesForMondayCursor(
                                        routeId = route,
                                        stopId = stop,
                                        directionId = direction,
                                        arrivalTime = arrival
                                    )
                            }
                            StarContract.Calendar.CalendarColumns.TUESDAY -> {
                                result =
                                    database.stopTimeRepository.stopTimesForTuesdayCursor(
                                        routeId = route,
                                        stopId = stop,
                                        directionId = direction,
                                        arrivalTime = arrival
                                    )
                            }
                            StarContract.Calendar.CalendarColumns.WEDNESDAY -> {
                                result =
                                    database.stopTimeRepository.stopTimesForWednesdayCursor(
                                        routeId = route,
                                        stopId = stop,
                                        directionId = direction,
                                        arrivalTime = arrival
                                    )
                            }
                            StarContract.Calendar.CalendarColumns.THURSDAY -> {
                                result =
                                    database.stopTimeRepository.stopTimesForThursdayCursor(
                                        routeId = route,
                                        stopId = stop,
                                        directionId = direction,
                                        arrivalTime = arrival
                                    )
                            }
                            StarContract.Calendar.CalendarColumns.FRIDAY -> {
                                result =
                                    database.stopTimeRepository.stopTimesForFridayCursor(
                                        routeId = route,
                                        stopId = stop,
                                        directionId = direction,
                                        arrivalTime = arrival
                                    )
                            }
                            StarContract.Calendar.CalendarColumns.SATURDAY -> {
                                result =
                                    database.stopTimeRepository.stopTimesForSaturdayCursor(
                                        routeId = route,
                                        stopId = stop,
                                        directionId = direction,
                                        arrivalTime = arrival
                                    )
                            }

                            StarContract.Calendar.CalendarColumns.SUNDAY -> {
                                result =
                                    database.stopTimeRepository.stopTimesForSundayCursor(
                                        routeId = route,
                                        stopId = stop,
                                        directionId = direction,
                                        arrivalTime = arrival
                                    )
                            }
                        }
                    }
                }
                QUERY_TRIPS -> {
                    if (selectionArgs != null && selectionArgs.size > 1) {
                        val route = selectionArgs[0]
                        val direction = selectionArgs[1]
                        result = database.stopRepository.stopsByRouteAndDirectionCursor(
                            routeId = route,
                            directionId = direction
                        )
                    }
                }

                QUERY_CALENDAR -> {
                    result = database.calendarRepository.calendarsCursor
                }
                QUERY_SEARCHED_STOPS -> {
                    if (selectionArgs != null && selectionArgs.isNotEmpty()) {
                        val stop = selectionArgs[0]
                        result = database.stopRepository.stopByNameCursor(
                            search = stop
                        )
                    }
                }
                QUERY_ROUTES_DETAILS -> {
                    if (selectionArgs != null && selectionArgs.size > 1) {
                        val trip = selectionArgs[0]
                        val arrival = selectionArgs[1]
                        result = database.stopTimeRepository.routeDetailsCursor(
                            tripId = trip,
                            arrivalTime = arrival
                        )
                    }
                }
                else -> {
                    if (selectionArgs != null && selectionArgs.isNotEmpty()) {
                        val stop = selectionArgs[0]
                        result = database.routeRepository.routesForStopByCursor(
                            stopName = stop
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Query Error...", e)
        }


        return result
    }

    override fun getType(uri: Uri): String? {
        var type: String? = null

        try {
            type = when (URI_MATCHER.match(uri)) {
                QUERY_ROUTES -> StarContract.BusRoutes.CONTENT_ITEM_TYPE
                QUERY_STOPS -> StarContract.Stops.CONTENT_ITEM_TYPE
                QUERY_CALENDAR -> StarContract.Calendar.CONTENT_ITEM_TYPE
                QUERY_STOP_TIMES -> StarContract.StopTimes.CONTENT_ITEM_TYPE
                QUERY_TRIPS -> StarContract.Trips.CONTENT_ITEM_TYPE
                QUERY_SEARCHED_STOPS -> StarContract.SearchedStops.CONTENT_ITEM_TYPE
                QUERY_ROUTES_FOR_STOP -> StarContract.RoutesForStop.CONTENT_ITEM_TYPE
                else -> StarContract.RouteDetails.CONTENT_ITEM_TYPE
            }
        } catch (th: Throwable) {
            Log.d(TAG, "Query Error...", th)
        }

        return type
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}