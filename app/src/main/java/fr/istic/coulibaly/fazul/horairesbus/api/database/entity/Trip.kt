package fr.istic.coulibaly.fazul.horairesbus.api.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract

@Entity(tableName = StarContract.Trips.CONTENT_PATH)
data class Trip(
        @PrimaryKey(autoGenerate = true) val _id: Int,
        @ColumnInfo(name = StarContract.Trips.TripColumns.BLOCK_ID)
        val blockId: Int,
        @ColumnInfo(name = StarContract.Trips.TripColumns.DIRECTION_ID)
        val directionId: Int,
        @ColumnInfo(name = StarContract.Trips.TripColumns.ROUTE_ID)
        val routeId: Int,
        @ColumnInfo(name = StarContract.Trips.TripColumns.SERVICE_ID)
        val serviceId: Int,
        @ColumnInfo(name = StarContract.Trips.TripColumns.HEADSIGN)
        val tripHeadSign: String,
        @ColumnInfo(name = StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE)
        val wheelchairAccessible: Boolean = false
)