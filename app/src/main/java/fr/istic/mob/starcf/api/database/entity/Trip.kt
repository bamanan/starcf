package fr.istic.mob.starcf.api.database.entity

import androidx.room.*
import fr.istic.mob.starcf.api.contract.StarContract

@Entity(
    tableName = StarContract.Trips.CONTENT_PATH,
//    foreignKeys = [
//        ForeignKey(
//            entity = BusRoute::class,
//            parentColumns = ["_id"],
//            childColumns = [StarContract.Trips.TripColumns.ROUTE_ID],
//            onDelete = ForeignKey.CASCADE
//        )
//    ],
    indices = [Index(
        value =
        [
            StarContract.Trips.TripColumns.BLOCK_ID,
            StarContract.Trips.TripColumns.DIRECTION_ID,
            StarContract.Trips.TripColumns.ROUTE_ID,
            StarContract.Trips.TripColumns.SERVICE_ID
        ],
        unique = true
    )]

)
data class Trip(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @ColumnInfo(name = StarContract.Trips.TripColumns.BLOCK_ID)
    val blockId: String,
    @ColumnInfo(name = StarContract.Trips.TripColumns.DIRECTION_ID)
    val directionId: String,
    @ColumnInfo(name = StarContract.Trips.TripColumns.ROUTE_ID)
    val routeId: String,
    @ColumnInfo(name = StarContract.Trips.TripColumns.SERVICE_ID)
    val serviceId: String,
    @ColumnInfo(name = StarContract.Trips.TripColumns.HEADSIGN)
    val tripHeadSign: String,
    @ColumnInfo(name = StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE)
    val wheelchairAccessible: String
)