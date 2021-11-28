package fr.istic.coulibaly.fazul.horairesbus.api.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoBase<T> {
   /**
    * Retrieve all records from database
    * @return List<T>
    */

   fun getAll() : Flow<List<T>>

   /**
    * Insert a record in database
    * @param record T
    * @return T
    */
   @Insert
   fun insert(record : T)

   /**
    * Insert a set of records in database
    * @param records List<T>
    * @return Boolean
    */
   @Insert
   fun insertAll(records : List<T>)

   /**
    * Delete a record from database
    * @param _id Int
    * @return Boolean
    */
   @Delete
   fun delete(record : T) : Int

   /**
    * Delete all records from database
    * @return Boolean
    */
   @Delete
   fun deleteAll()
}