package fr.istic.coulibaly.fazul.horairesbus.api.core.services

import androidx.annotation.Keep

@Keep
data class ApiResponse(
    val nhits: Int,
    val parameters: Parameters,
    val records: List<Record>
) {
    @Keep
    data class Parameters(
        val dataset: String,
        val format: String,
        val rows: Int,
        val start: Int,
        val timezone: String
    )

    @Keep
    data class Record(
        val datasetid: String,
        val fields: Fields,
        val record_timestamp: String,
        val recordid: String
    ) {
        @Keep
        data class Fields(
            val debutvalidite: String,
            val description: String,
            val fichier: Fichier,
            val finvalidite: String,
            val id: String,
            val publication: String,
            val tailleoctets: Int,
            val url: String
        ) {
            @Keep
            data class Fichier(
                val filename: String,
                val format: String,
                val height: Int,
                val id: String,
                val mimetype: String,
                val thumbnail: Boolean,
                val width: Int
            )
        }
    }
}