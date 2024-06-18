package di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import database.DriverFactory
import org.koin.dsl.module

val jsModule = module {
    single { LifecycleRegistry() }
    single<ComponentContext> { DefaultComponentContext(lifecycle = get<LifecycleRegistry>()) }
    single { DriverFactory() }
}

fun startKoin() = initKoin(additionalModules = listOf(jsModule))
// Android에서 initKoin초기화할때에는 따로 additionalModules을 안넣어줬었는데, DesktopApp쪽에서는 여기서 사용할 전용 모듈을 추가해준 모습