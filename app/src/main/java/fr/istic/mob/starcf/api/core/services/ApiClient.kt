package fr.istic.mob.starcf.api.core.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiClient {
    companion object {
        const val BASE_URL = "https://data.explore.star.fr"
        const val FILE_BASE_URL =
            "http://ftp.keolis-rennes.com/opendata/tco-busmetro-horaires-gtfs-versions-td/attachments/"
        const val END_POINT =
            "/api/records/1.0/search/?dataset=tco-busmetro-horaires-gtfs-versions-td"
    }

    @GET(END_POINT)
    suspend fun getLatestCalendar(): Response<ApiResponse>

    @GET
    fun downloadLatestCalendar(@Url fileUrl: String): Call<ResponseBody>
}