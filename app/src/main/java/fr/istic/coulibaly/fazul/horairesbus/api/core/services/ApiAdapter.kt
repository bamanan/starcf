package fr.istic.coulibaly.fazul.horairesbus.api.core.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAdapter {
    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl(ApiClient.BASE_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)

    val apiDownloadClient: ApiClient = Retrofit.Builder()
        .baseUrl(ApiClient.FILE_BASE_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)
}