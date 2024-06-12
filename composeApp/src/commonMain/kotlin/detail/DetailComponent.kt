package detail

import HomeViewModel
import androidx.compose.foundation.interaction.PressInteraction
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

interface DetailComponent {

    val model: Value<Model> // 상태 관리
    fun onBackPressed()

    data class Model (
        val item: Product
    )
}

class DefaultDetailComponent(
    private val componentContext: ComponentContext,
    private val item: Product,
    private val onBack: () -> Unit,
): DetailComponent, ComponentContext by componentContext {
    private val _model = MutableValue(DetailComponent.Model(item = item))
    override val model: Value<DetailComponent.Model> = _model
    override fun onBackPressed() {
        onBack()
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
           // 만약 API 호출이 필요하면 여기서 하면 됨.
        }
    }
}