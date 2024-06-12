package detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.Text
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import list.ListComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    component: DetailComponent
) {

    val product = component.model.subscribeAsState()

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets(0.dp)),
                    title = { Text("Product Detail") },
                    navigationIcon = {
                        IconButton(onClick = {
                            component.onBackPressed()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                        }
                    }
                )
            }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(16.dp)
            ){
                Text("Product Title: ${product.value.item.title}")
                Text("Product Desc: ${product.value.item.description}")
            }
        }
    }
}