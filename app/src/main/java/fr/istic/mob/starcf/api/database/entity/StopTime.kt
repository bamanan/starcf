package fr.istic.mob.starcf.api.database.entity

import androidx.room.*
import fr.istic.mob.starcf.api.contract.StarContract

@Entity(
    tableName = StarContract.StopTimes.CONTENT_PATH,
//    foreignKeys = [
//        ForeignKey(
//            entity = Trip::class,
//            parentColumns = ["_id"],
//            childColumns = [StarContract.StopTimes.StopTimeColumns.TRIP_ID],
//            onDelete = CASCADE
//        ),
//        ForeignKey(
//            entity = StopTime::class,
//            parentColumns = ["_id"],
//            childColumns = [StarContract.StopTimes.StopTimeColumns.STOP_ID],
//            onDelete = CASCADE
//        )
//    ],
    indices = [Index(
        value =
        [
            StarContract.StopTimes.StopTimeColumns.TRIP_ID,
            StarContract.StopTimes.StopTimeColumns.STOP_ID,
        ],
        unique = true
    )]
)
data class StopTime(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.TRIP_ID)
    val tripId: String,
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.STOP_ID)
    val stopId: String,
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
    val arrivalTime: String,
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME)
    val departureTime: String,
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE)
    val stopSequence: String
)