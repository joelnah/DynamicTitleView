package na.family.prayer.library.data

import androidx.compose.ui.geometry.Size

data class ImageData<T>(
    val image: T? = null,
    val imageSize: Size? = null
) {
    inline fun <reified R> getImageAs(): R? {
        return image as? R
    }
}
