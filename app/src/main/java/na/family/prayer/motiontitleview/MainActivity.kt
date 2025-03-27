package na.family.prayer.motiontitleview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import na.family.prayer.library.data.TitleData
import na.family.prayer.library.data.ImageData
import na.family.prayer.library.ui.DynamicTitleView
import na.family.prayer.motiontitleview.ui.theme.MotionTitleViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotionTitleViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier,
    ) {

        DynamicTitleView(
            scrollState = scrollState,
            data = TitleData(
                firstImage = ImageData(Icons.Default.Info),
                secondText = "Information",
            ),
        )
        Column(
            modifier = Modifier.verticalScroll(scrollState),
        ) {
            repeat(30){
                Text(
                    text = "$it Hello $name!",
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                )
            }
        }

    }

}