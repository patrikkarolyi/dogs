package com.example.dogs.di

import android.content.Context
import com.example.dogs.data.network.DogAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(DogAPI.baseUrl)
        .client(client)
        .addConverterFactory(
            MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor( HttpLoggingInterceptor()
                .apply {
                    level = HttpLoggingInterceptor.Level.NONE
                })
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideWeatherAPI(retrofit: Retrofit): DogAPI = retrofit.create(DogAPI::class.java)
}