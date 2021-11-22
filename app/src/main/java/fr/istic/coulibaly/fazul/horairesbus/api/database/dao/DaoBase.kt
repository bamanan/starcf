package fr.istic.coulibaly.fazul.horairesbus.api.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface DaoBase<T> {
   /**
    * Retrieve all records from database
    * @return List<T>
    */

   fun getAll() : List<T>

   /**
    * Insert a record in database
    * @param record T
    * @return T
    */
   @Insert
   fun insert(record : T) : T

   /**
    * Insert a set of records in database
    * @param records List<T>
    * @return Boolean
    */
   @Insert
   fun insertAll(records : List<T>) : Boolean

   /**
    * Delete a record from database
    * @param _id Int
    * @return Boolean
    */
   @Delete
   fun delete(record : T) : Boolean

   /**
    * Delete all records from database
    * @return Boolean
    */
   @Delete
   fun deleteAll() : Boolean
}