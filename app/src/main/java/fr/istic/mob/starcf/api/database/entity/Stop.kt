package fr.istic.mob.starcf.api.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import fr.istic.mob.starcf.api.contract.StarContract

@Entity(
    tableName = StarContract.Stops.CONTENT_PATH,
    indices = [Index(
        value =
        [
            StarContract.Stops.StopColumns.NAME,
            StarContract.Stops.StopColumns.DESCRIPTION,
            StarContract.Stops.StopColumns.LATITUDE,
            StarContract.Stops.StopColumns.LONGITUDE
        ],
        unique = true
    )]
)
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
    val wheelchairBoarding: String
)