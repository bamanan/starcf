package fr.istic.mob.starcf.api.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface DaoBase<T> {
    /**
     * Retrieve all records from database
     * @return LiveData<List<T>>
     */
    fun all(): LiveData<List<T>>

    /**
     * Retrieve all records from database
     * @return List<T>
     */
    fun list(): List<T>

    /**
     * Insert a record in database
     * @param record T
     * @return T
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: T)

    /**
     * Insert a set of records in database
     * @param records List<T>
     * @return Boolean
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg records: T)

    /**
     * Delete a record from database
     * @param record T
     **/
    @Delete
    fun delete(record: T): Int

    /**
     * Delete all records from database
     * @return Boolean
     */
    @Delete
    fun deleteAll()
}