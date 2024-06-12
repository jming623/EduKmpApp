package root

import HomeViewModel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import data.Product
import detail.DefaultDetailComponent
import detail.DetailComponent
import kotlinx.serialization.Serializable
import list.DefaultListComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>> // Value객체 = Decompose 라이브러리에서 제공하는 상태 관리 객체 (Compose에 state와 유사)
    fun onBackClicked()
    sealed class Child { // sealed class = 특정 상속 구조를 정의할 때 사용되는 클래스, when 표현식을 위해 사용(실제 사용 목적은 더 있다고 함.)
        class ListChild(val component: DefaultListComponent): Child()

        class DetailChild(val component: DetailComponent): Child()
    }
}
//Component -> Config(pushing in navigation and passing data) -> Deciding the Child(childFactory) -> Deciding The UI(RootContent)

class DefaultRootComponent(
    private val componentContext: ComponentContext,
    private val homeViewModel: HomeViewModel,
): RootComponent, ComponentContext by componentContext{

    // StackNavigation = 화면 전환을 스택 구조로 관리할 수 있는 Decompose 라이브러리의 클래스
    // 화면을 스택에 추가하거나 제거하여, 사용자가 뒤로 이동할 수 있는 네비게이션 스택을 관리 + 스택의 상태를 유지하고 복원할 수 있어, 화면 전환 시 상태를 쉽게 관리
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
    childStack(
        source = navigation,
        serializer = Config.serializer(), // Serializable이 선언된 sealed class를 serializer함수로 (역)직렬화 해서 사용 가능
        initialConfiguration = Config.List, // 애플리케이션 시작 시 첫 번째로 표시될 화면을 지정
        handleBackButton = true, // 뒤로 가기 버튼을 처리할지 여부
        childFactory = ::childFactory // "::" 는 참조를 의미
    )// 이 부분이 Value<ChildStack<*, RootComponent.Child>>형태로 반환됨.
    
    fun childFactory(config: Config, componentContext: ComponentContext): RootComponent.Child{ // RootComponent에 Child를 상속
        return when (config) {
            is Config.List -> RootComponent.Child.ListChild(
                DefaultListComponent(componentContext, homeViewModel) { item ->
                    navigation.push(Config.Detail(item))
                    // it will change the content to Detail
                }
            )

            is Config.Detail -> RootComponent.Child.DetailChild(
                DefaultDetailComponent(componentContext, config.item) {
                    onBackClicked()
                    // it will change the content to Detail
                }
            )
        }
        /*
            when (조건)
            is 비교대상 -> true시 실행될 코드
            ...
            else -> 부합하는 조건이 없을 때 실행될 코드
         */
    }

    override fun onBackClicked() {
        navigation.pop()
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object List: Config // data object는 매개변수 없는 data class, 싱글톤 객체 특징
        @Serializable
        data class Detail(val item: Product): Config // data class는 데이터를 담기위한 클래스
    }
}