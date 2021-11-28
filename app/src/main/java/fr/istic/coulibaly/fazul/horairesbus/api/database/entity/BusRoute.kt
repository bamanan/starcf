package fr.istic.coulibaly.fazul.horairesbus.api.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract

@Entity(tableName = StarContract.BusRoutes.CONTENT_PATH)
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