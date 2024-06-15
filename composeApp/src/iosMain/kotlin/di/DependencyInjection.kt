package di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.koin.core.Koin
import org.koin.dsl.module
import root.RootComponent

// iOS-specific 의존성을 위한 Koin 모듈 정의
val iosModule = module {
    // LifecycleRegistry를 싱글톤으로 등록
    single { LifecycleRegistry() }

    // LifecycleRegistry로 초기화된 DefaultComponentContext를 사용하여 ComponentContext를 등록
    single<ComponentContext> { DefaultComponentContext(lifecycle = get<LifecycleRegistry>()) }
}

fun initKoinIOS() = initKoin(additionalModules = listOf(iosModule)) // iOS용 Koin을 초기화하는 함수

val Koin.rootComponent: RootComponent // Koin에서 RootComponent를 가져오는 확장 프로퍼티(외부에서 호출하기 위함)
    get() = get()

val Koin.lifecycleRegistry: LifecycleRegistry
    get() = get()


/*
 여기에다가 Swift관련 코드까지 설명을 해주는데, 아직 스위프트 환경 구성이 되어있지 않아서, 만약 추후에 궁금하면 아래 링크 6번째영상 24분쯤으로 확인하고,
 https://www.youtube.com/playlist?list=PL7W-WmzNxofK8lWAlb-v_6V1d3AOq0kub
 여기서 설명하는 IosApp관련 초기 구성 영상은 아마 재생목록 5번에 있을 듯 함.
*/