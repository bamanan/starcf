package fr.istic.mob.starcf.api.contract

import android.net.Uri

internal interface StarContract {

    companion object {
        const val AUTHORITY = "fr.istic.mob.starcf"
        val AUTHORITY_URI: Uri = Uri.parse("content://$AUTHORITY")
        const val ROUTES = "routes"
        const val STOPS = "stops"
        const val TRIPS = "trips"
        const val STOP_TIMES = "stop_times"
        const val CALENDAR = "calendar"

    }

    interface BusRoutes {
        interface BusRouteColumns : BaseColumns {
            companion object {
                const val ID = BaseColumns.ID
                const val SHORT_NAME = "route_short_name"
                const val LONG_NAME = "route_long_name"
                const val DESCRIPTION = "route_desc"
                const val TYPE = "route_type"
                const val COLOR = "route_color"
                const val TEXT_COLOR = "route_text_color"
            }
        }

        companion object {
            const val CONTENT_PATH = "busroutes"
            val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH)
            const val CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.fr.istic.mob.starcf.busroutes"
            const val CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.fr.istic.mob.starcf.busroute"
        }
    }

    interface Trips {
        interface TripColumns : BaseColumns {
            companion object {
                const val ID = BaseColumns.ID
                const val ROUTE_ID = "route_id"
                const val SERVICE_ID = "service_id"
                const val HEADSIGN = "trip_headsign"
                const val DIRECTION_ID = "direction_id"
                const val BLOCK_ID = "block_id"
                const val WHEELCHAIR_ACCESSIBLE = "wheelchair_accessible"
            }
        }

        companion object {
            const val CONTENT_PATH = "trips"
            val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH)
            const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.fr.istic.mob.starcf.trips"
            const val CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.fr.istic.mob.starcf.trip"
        }
    }

    interface Stops {
        interface StopColumns : BaseColumns {
            companion object {
                const val ID = BaseColumns.ID
                const val NAME = "stop_name"
                const val DESCRIPTION = "stop_desc"
                const val LATITUDE = "stop_lat"
                const val LONGITUDE = "stop_lon"
                const val WHEELCHAIR_BOARDING = "wheelchair_boarding"
            }
        }

        companion object {
            const val CONTENT_PATH = "stops"
            val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH)
            const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.fr.istic.mob.starcf.stops"
            const val CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.fr.istic.mob.starcf.stop"
        }
    }

    interface StopTimes {
        interface StopTimeColumns : BaseColumns {
            companion object {
                const val ID = BaseColumns.ID
                const val TRIP_ID = "trip_id"
                const val ARRIVAL_TIME = "arrival_time"
                const val DEPARTURE_TIME = "departure_time"
                const val STOP_ID = "stop_id"
                const val STOP_SEQUENCE = "stop_sequence"
            }
        }

        companion object {
            const val CONTENT_PATH = "stoptimes"
            val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH)

            // select stop_time.*, trip.*, calendar.*
            const val CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.fr.istic.mob.starcf.stoptimes"
            const val CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.fr.istic.mob.starcf.stoptime"
        }
    }

    interface Calendar {
        interface CalendarColumns : BaseColumns {
            companion object {
                const val ID = BaseColumns.ID
                const val MONDAY = "monday"
                const val TUESDAY = "tuesday"
                const val WEDNESDAY = "wednesday"
                const val THURSDAY = "thursday"
                const val FRIDAY = "friday"
                const val SATURDAY = "saturday"
                const val SUNDAY = "sunday"
                const val START_DATE = "start_date"
                const val END_DATE = "end_date"
            }
        }

        companion object {
            const val CONTENT_PATH = "calendar"
            val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH)
            const val CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.fr.istic.mob.starcf.calendars"
            const val CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.fr.istic.mob.starcf.calendar"
        }
    }

    interface RouteDetails {
        companion object {
            const val CONTENT_PATH = "routedetails"
            val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH)

            // select stop.stop_name, stop_time.arrival_time
            const val CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.fr.istic.mob.starcf.routedetails"
            const val CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.fr.istic.mob.starcf.routedetail"
        }
    }

    interface SearchedStops {
        companion object {
            const val CONTENT_PATH = "searchedstops"
            val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH)
            const val CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.fr.istic.mob.starcf.searchedstops"
            const val CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.fr.istic.mob.starcf.searchedstop"
        }
    }

    interface RoutesForStop {
        companion object {
            const val CONTENT_PATH = "routesforstop"
            val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH)
            const val CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.fr.istic.mob.starcf.routesforstops"
            const val CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.fr.istic.mob.starcf.routesforstop"
        }
    }


}