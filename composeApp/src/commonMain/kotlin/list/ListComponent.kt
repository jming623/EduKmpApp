package list

import HomeViewModel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

interface ListComponent {

    val model: Value<Model> // 상태 관리
    fun onItemClicked(item: Product)

    data class Model (
        val items: List<Product>
    )
}

/*
    아래 DefaultListComponent클래스로 보는 Kotlin Class Tip!

    class DefaultListComponent ( private val componentContext: ComponentContext, ... ) : ListComponent, ComponentContext by componentContext

    정의 :
    DefaultListComponent 이름의 class는 ComponentContext타입의 componentContext변수를 매개변수로 받고 + ...,
    ListComponent와 ComponentContext를 상속 받는데, 이때 ComponentContext의 구현은 componentContext객체에게 위임한다.

    ":타입" :
    클래스 뒤에오는 ":타입"은 특정 인터페이스를 구현하거나 다른 클래스를 상속한다는 의미이고
    매개변수 뒤에오는 ":타입"은 매개변수의 타입을 의미함.

    by 키워드:
    1. 클래스 위임. 2. 프로퍼티 위임(이건 다음에 설명)
    여기서의 의미는
    DefaultListComponent 클래스는 ComponentContext 인터페이스의 구현을 매개변수로 전달받은 componentContext 객체에 위임합니다.

    ComponentContext란?
    Decompose의 기능으로 매우 중요한 매개변수로 사용됨.
    주요 기능: UI 구성 요소의 수명 주기 및 상태를 관리
    1. LifecycleOwner = 수명 주기 이벤트 관리
    2. StateKeeperOwner = 변경 또는 프로세스 재생성 중에 상태를 유지
    3. InstanceKeeperOwner = 수명 주기 동안 유지해야 하는 개체의 인스턴스를 관리
    4. BackHandlerOwner = 뒤로가기 버튼 이벤트간의 처리

    함수 매개변수:
    class DefaultListComponent(..., private val onItemSelected: (item: Product) -> Unit) ...
    을 보면 매개변수로 람다 형식의 함수를 받고있음.

    (parameter: ParameterType) -> ReturnType 이런 형태가 되는데
    여기서 ReturnType의 Unit은 Kotlin에서 값이 없음을 나타내는 타입.

    이건 나중에 사용될 때
    DefaultListComponent(1st param, 2nd param) { selectedProduct ->
        println("Selected Product: $selectedProduct")
    }
    간단하게 사용하면 이런식으로 사용될 수 있는데, 생각에는 매개변수이니 소괄호 안에 들어가야할 것 같지만,
    Kotlin Class 인스턴스 생성 시, 마지막 매개변수가 함수라면 소괄호 밖으로 빼서 "Trailing Lambda" 라는 표현식으로 사용 할 수 있다고 함.
    물론 두개가되면,
    val component = DefaultListComponent(
        componentContext = componentContext,
        homeViewModel = homeViewModel,
        onItemSelected = { selectedProduct ->
            println("Selected Product: $selectedProduct")
        },
        onItemClicked = { clickedProduct ->
            println("Clicked Product: $clickedProduct")
        }
    )
    이런식으로 작성해야 한다고 함.
)
 */

class DefaultListComponent(
    private val componentContext: ComponentContext,
    private val homeViewModel: HomeViewModel,
    private val onItemSelected: (item: Product) -> Unit
): ListComponent, ComponentContext by componentContext {

    // 가변적인 변수를 _model를 통해 내부에서 컨트롤하고, 외부에는 model을 노출시킴
    private val _model = MutableValue<ListComponent.Model>(ListComponent.Model(items = emptyList()))
    override val model: Value<ListComponent.Model> = _model
    override fun onItemClicked(item: Product) {
        onItemSelected(item)
    }

    init {
        /*
            CoroutineScope - 비동기
            launch함수는 코루틴 시작하고, 비동기로 실행할 코드를 작성하는 함수

            CoroutineScope(Dispatchers.Default):
            백그라운드 스레드에서 코루틴을 실행하는 데 사용

            collect함수:
            Flow에서 값을 수집하고 해당 값을 처리하는 함수를 인자로 받음 (람다, 익명)
            homeViewModel쪽을 보면 우리가 products를 asStateFlow()로 Flow화 시켜놓았음.
            이 Flow들을 수집해서 내보내느 값을 "it"으로 전달받아 사용 가능
        */

        CoroutineScope(Dispatchers.Default).launch {
            homeViewModel.products.collect{
                _model.value = ListComponent.Model(items = it)
            }
        }
    }
}