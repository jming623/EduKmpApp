import data.Product
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
): ViewModel() {

    private  val _products = MutableStateFlow<List<Product>>(listOf()) // listof() = 빈 list 반환
    val products = _products.asStateFlow()

    // 클래스 내부에서 직접 초기화하지 않고 Koin을 통해 주입받는 형태
    // 객체가 클래스 외부에서 초기화되면 객체가 클래스의 상태에 영향을 받는 현상을 방지할 수 있다.
    //private val homeRepository = HomeRepository()

    init {
        viewModelScope.launch {
            homeRepository.getProducts().collect { products ->
                _products.update { it + products }
            }
        }
    }
}