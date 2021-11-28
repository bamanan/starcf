package fr.istic.coulibaly.fazul.horairesbus.api.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract

@Entity(tableName = StarContract.Calendar.CONTENT_PATH)
data class Calendar(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.START_DATE)
    val startDate: String,
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.END_DATE)
    val endDate: String,
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.MONDAY)
    val monday: String,
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.TUESDAY)
    val tuesday: String,
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.WEDNESDAY)
    val wednesday: String,
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.THURSDAY)
    val thursday: String,
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.FRIDAY)
    val friday: String,
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.SATURDAY)
    val saturday: String,
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.SUNDAY)
    val sunday: String
)