package di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun networkModule() = module {
    single {
        HttpClient { // HttpClient 객체는 빌더 패턴을 통해 생성됨. 빌더 패턴을 사용하면 HttpClient 객체는 빌더 패턴 내에서 설정한 기본값이나 사용자 지정 설정 바탕으로 초기화 됨.
            install(ContentNegotiation){ //ContentNegotiation 플러그인을 설치한다. ContentNegotiation은 HTTP 요청 및 응답에서 JSON 데이터를 자동으로 직렬화하고 역직렬화할 수 있도록 도와줌.
                json(Json { // JSON 직렬화 설정
                    prettyPrint = true // JSON을 읽기 쉽게 보여줄지 여부
                    ignoreUnknownKeys = true //알수없는 키 무시할지 여부
                })
            }
        }
    }
}