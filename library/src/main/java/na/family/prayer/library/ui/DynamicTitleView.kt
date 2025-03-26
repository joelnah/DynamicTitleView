package na.family.prayer.library.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import na.family.prayer.library.data.TitleData
import na.family.prayer.library.domain.ImagePosition
import na.family.prayer.library.utils.DpSaver
import kotlin.math.min

@Composable
fun DynamicTitleView(
    modifier: Modifier = Modifier, // Modifier to customize the view's appearance
    scrollState: ScrollableState, // State of the scrollable component
    data: TitleData, // Data containing title and images
    style: TextStyle = MaterialTheme.typography.titleLarge, // Text style applied to the titles
    onFractionChanged: ((Float) -> Unit)? = null // Callback triggered with updated fraction value
) {
    val density = LocalDensity.current // Retrieves the current density for pixel-to-DP conversions
    val maxOffset = with(density) { 200.dp.toPx() } // Maximum offset in pixels
    var defaultSize by rememberSaveable(stateSaver = DpSaver) { mutableStateOf(style.fontSize.value.dp) } // Default font size saved across recompositions
    var firstTextHeight by remember { mutableFloatStateOf(0f) } // Height of the first text component
    var firstTextWith by remember { mutableFloatStateOf(0f) } // Width of the first text component
    var firstTextSize by remember { mutableStateOf(defaultSize) } // Font size of the first text
    var secondTextSize by remember { mutableStateOf(defaultSize) } // Font size of the second text
    var itemScrollOffset by remember { mutableIntStateOf(0) } // Offset value for scrolling items

    // Check if scrollState is of type LazyListState
    if (scrollState is LazyListState) {
        val firstOffset by remember { derivedStateOf { scrollState.firstVisibleItemScrollOffset } } // Offset of the first visible item in the LazyListState
        LaunchedEffect(firstOffset) { // Effect to monitor changes in firstOffset
            firstOffset.let { currentOffset ->
                val currentIndex = scrollState.firstVisibleItemIndex // Index of the first visible item

                if (itemScrollOffset < currentOffset || currentIndex == 0) {
                    itemScrollOffset = currentOffset // Update itemScrollOffset if conditions are met
                }
            }
        }
    }

    // Retrieve scroll position based on the type of scrollState
    val scrollOffset by remember {
        derivedStateOf {
            when (scrollState) {
                is ScrollState -> scrollState.value.toFloat() // Scroll position for ScrollState
                is LazyListState -> itemScrollOffset // Scroll position for LazyListState
                // TODO: Add more support in the future
//                is LazyGridState -> {
//                    if(scrollState.firstVisibleItemIndex == 0) {
//                        itemScrollOffset = scrollState.firstVisibleItemScrollOffset.toFloat()
//                    }
//                    itemScrollOffset
//                }
                else -> 0f // Default scroll position for unsupported types
            }
        }
    }

    val fraction by remember { // Fraction representing the scroll progress
        derivedStateOf {
            val offset = min(scrollOffset.toFloat(), maxOffset) // Limit the offset to maxOffset
            (offset / maxOffset).coerceIn(0f, 1f) // Calculate the fraction within the range of 0 to 1
        }
    }

    val boxSize by remember { // Size of the box containing the text
        derivedStateOf {
            val newSize = lerp(firstTextSize + secondTextSize, firstTextSize, fraction) // Interpolates between sizes based on fraction
            defaultSize = newSize // Store the current boxSize
            newSize
        }
    }

    Row( // Row layout to arrange text and images
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        val textSize by remember { // Dynamic text size based on fraction
            derivedStateOf {
                lerp(style.fontSize, style.fontSize / 1.4, fraction)
            }
        }

        val textYTranslation by remember { // Translation for the Y-axis of text
            derivedStateOf {
                lerp(firstTextWith, 0f, fraction)
            }
        }

        val textXTranslation by remember { // Translation for the X-axis of text
            derivedStateOf {
                lerp(0f, firstTextHeight + textSize.value, fraction)
            }
        }

        onFractionChanged?.invoke(fraction) // Trigger the callback with updated fraction value

        Box( // Box to contain text components
            modifier = Modifier
                .height(boxSize)
                .align(Alignment.CenterVertically),
        ) {
            Row( // Row for the first text and its image
                modifier = Modifier.onGloballyPositioned { coordinates -> // Capture the size and position of the first text
                    firstTextHeight = coordinates.size.width.toFloat()
                    firstTextWith = coordinates.size.height.toFloat()
                    firstTextSize = with(density) { coordinates.size.height.toDp() }
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RenderImage(ImagePosition.First, data, fraction) // Render the first image
                Text(
                    text = data.title.first, // Display the first title
                    style = style.copy(fontSize = textSize), // Apply dynamic text size
                )
            }

            Row( // Row for the second text and its image
                modifier = Modifier
                    .graphicsLayer {
                        translationX = textXTranslation // Apply X-axis translation
                        translationY = textYTranslation // Apply Y-axis translation
                    }
                    .onGloballyPositioned { coordinates -> // Capture the size of the second text
                        secondTextSize = with(density) { coordinates.size.height.toDp() }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RenderImage(ImagePosition.Second, data, fraction) // Render the second image
                if (data.title.second.isNotEmpty()) { // Check if second title is not empty
                    Text(
                        text = data.title.second, // Display the second title
                        modifier = Modifier.weight(1f, fill = false),
                        style = style.copy(fontSize = textSize), // Apply dynamic text size
                    )
                }
            }
        }
    }
}
