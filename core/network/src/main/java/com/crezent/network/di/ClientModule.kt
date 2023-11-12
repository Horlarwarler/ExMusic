package com.crezent.network.di

import com.crezent.common.preference.Preference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {

    @Singleton
    @Provides
    fun providesHttp(
        preference: Preference
    ): HttpClient {
        val token = preference.apiKey()

        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }

                )

            }
            install(WebSockets  ){
                pingInterval = 300_000
               //this.
            }
            install(HttpTimeout) {
                val timeOutMillis: Long = 60000
                connectTimeoutMillis = timeOutMillis
                requestTimeoutMillis = timeOutMillis
                socketTimeoutMillis = timeOutMillis
            }

            defaultRequest {
                url("http://10.42.0.1:8080/")
                token?.let {
                    header("Authorization", "Bearer $it")
                }

            }

        }
    }

}