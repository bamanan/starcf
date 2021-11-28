package fr.istic.coulibaly.fazul.horairesbus.api.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract

@Entity(tableName = StarContract.StopTimes.CONTENT_PATH)
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