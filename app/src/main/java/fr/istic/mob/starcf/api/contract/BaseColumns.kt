package fr.istic.mob.starcf.api.contract


interface BaseColumns {
    companion object {
        /**
         * The unique ID for a row.
         *
         *
         * Type: INTEGER (long)
         */
        // @Column(Cursor.FIELD_TYPE_INTEGER)
        const val ID = "_id"

        /**
         * The count of rows in a directory.
         *
         *
         * Type: INTEGER
         */
        // @Column(Cursor.FIELD_TYPE_INTEGER)
        const val COUNT = "_count"
    }
}
