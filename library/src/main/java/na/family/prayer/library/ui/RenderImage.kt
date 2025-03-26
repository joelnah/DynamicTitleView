package na.family.prayer.library.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import na.family.prayer.library.data.TitleData
import na.family.prayer.library.domain.ImagePosition

@Composable
internal fun RenderImage(position: ImagePosition, data: TitleData, fraction: Float) {
    val (vectorData, stringData) = when (position) {
        ImagePosition.First -> Pair(data.getFirstImageAs<ImageVector>(), data.firstImage) // Retrieve data for the first image
        ImagePosition.Second -> Pair(data.getSecondImageAs<ImageVector>(), data.secondImage) // Retrieve data for the second image
    }

    vectorData?.let { // If vector data exists
        val defaultWidth: Float = vectorData.defaultWidth.value
        val defaultHeight: Float = vectorData.defaultHeight.value

        val width by remember(fraction) { // Dynamic width based on fraction
            derivedStateOf {
                lerp(defaultWidth.dp, (defaultWidth / 1.4).dp, fraction)
            }
        }
        val height by remember(fraction) { // Dynamic height based on fraction
            derivedStateOf {
                lerp(defaultHeight.dp, (defaultHeight / 1.4).dp, fraction)
            }
        }

        Image( // Render the vector image
            modifier = Modifier.size(width, height),
            imageVector = vectorData,
            contentDescription = ""
        )

    } ?: stringData?.let { // If string data exists
        val imageLink = stringData.getImageAs<String>() // Retrieve image link as a string
        val defaultWidth: Float
        val defaultHeight: Float
        when {
            stringData.imageSize != null -> { // If image size is specified
                defaultWidth = stringData.imageSize.width // Use specified width
                defaultHeight = stringData.imageSize.height // Use specified height
            }
            else -> { // Default to 40x40 if size is not specified
                defaultWidth = 40f
                defaultHeight = 40f
            }
        }

        val width by remember(fraction) { // Dynamic width based on fraction
            derivedStateOf {
                lerp(defaultWidth.dp, (defaultWidth / 1.4).dp, fraction)
            }
        }
        val height by remember(fraction) { // Dynamic height based on fraction
            derivedStateOf {
                lerp(defaultHeight.dp, (defaultHeight / 1.4).dp, fraction)
            }
        }

        AsyncImage(
            modifier = Modifier.size(width, height),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageLink)
                .decoderFactory(SvgDecoder.Factory()) // SVG decoding factory
                .crossfade(true)
                .build(),
            filterQuality = FilterQuality.Low,
            contentScale = ContentScale.Fit,
            contentDescription = ""
        )
    }
}
