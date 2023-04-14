package com.carlosgub.rmgraphql.core.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Singleton
    @Provides
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient =
        ApolloClient.Builder()
            .okHttpClient(okHttpClient)
            .serverUrl(BASE_URL)
            .build()

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val okHttp = OkHttpClient().newBuilder()
            .callTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        return okHttp.build()
    }

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/graphql/"
    }
}
