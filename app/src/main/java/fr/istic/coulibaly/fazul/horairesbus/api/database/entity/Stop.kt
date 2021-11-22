package fr.istic.coulibaly.fazul.horairesbus.api.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract

@Entity(tableName = StarContract.Stops.CONTENT_PATH)
data class Stop(
        @PrimaryKey(autoGenerate = true) val _id: Int = 0,
        @ColumnInfo(name = StarContract.Stops.StopColumns.NAME)
        val name: String,
        @ColumnInfo(name = StarContract.Stops.StopColumns.DESCRIPTION)
        val description: String,
        @ColumnInfo(name = StarContract.Stops.StopColumns.LATITUDE)
        val latitude: String,
        @ColumnInfo(name = StarContract.Stops.StopColumns.LONGITUDE)
        val longitude: String,
        @ColumnInfo(name = StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING)
        val wheelchairBoarding: Int
)