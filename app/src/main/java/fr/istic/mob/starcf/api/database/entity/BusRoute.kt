package fr.istic.mob.starcf.api.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import fr.istic.mob.starcf.api.contract.StarContract

@Entity(
    tableName = StarContract.BusRoutes.CONTENT_PATH,
    indices = [Index(value =
        [
            StarContract.BusRoutes.BusRouteColumns.SHORT_NAME,
            StarContract.BusRoutes.BusRouteColumns.LONG_NAME,
            StarContract.BusRoutes.BusRouteColumns.DESCRIPTION
        ],
        unique = true)]
)
data class BusRoute(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.SHORT_NAME)
    val shortName: String,
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.LONG_NAME)
    val longName: String,
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.DESCRIPTION)
    val description: String,
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.TYPE)
    val type: String,
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.COLOR)
    val color: String,
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR)
    val textColor: String
)