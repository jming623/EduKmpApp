package di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    additionalModules: List<Module> = emptyList(), // 아무것도 안들어오면 기본값을 빈 리스트로 한다
    appDeclaration: KoinAppDeclaration = {} // appDeclaration매개변수를 통해 호출자가 koin의 추가설정을 할 수 있도록하는 매우 유연한 접근 방식
) = startKoin { // koin초기화 명령
    appDeclaration()
    modules(additionalModules + commonModule() + platformModule())
    //additionalModules에 commonModule과 platformModule을 결합하고 있음. Module객체를 반환해야함
    // commonModule과 platformModule은 올바르게 Module객체를 반환해야 함
}
