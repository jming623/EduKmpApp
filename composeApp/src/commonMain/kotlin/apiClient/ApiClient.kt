package apiClient

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    install(ContentNegotiation){
        json(Json {
            prettyPrint = true //디버깅
            ignoreUnknownKeys = true //알수없는 키 무시
        })
    }
}