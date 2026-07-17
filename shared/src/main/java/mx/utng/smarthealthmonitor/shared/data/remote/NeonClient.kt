package mx.utng.smarthealthmonitor.shared.data.remote

import mx.utng.smarthealthmonitor.shared.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NeonClient {
    private const val BASE_URL = "https://${BuildConfig.NEON_HOST}/"
 
    val CONN_STRING  = "postgresql://neondb_owner:npg_ipCW37tueZNw@ep-wild-dawn-ai7pzn8b-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require"
 
    val api: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .hostnameVerifier { _, _ -> true } // Added from user's troubleshooting section
                .build())
            .build()
            .create(NeonApiService::class.java)
    }
}
