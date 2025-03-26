package na.family.prayer.library.data


// Data class for storing motion title data, including text and images
data class TitleData(
    val firstImage: ImageData<*>? = null, // The first image associated with the title (nullable)
    val secondImage: ImageData<*>? = null, // The second image associated with the title (nullable)
    val firstText: String = "", // The first text in the title (defaults to an empty string)
    val secondText: String = "", // The second text in the title (defaults to an empty string)
) {
    // Computed property to return a pair of the first and second texts as a title
    val title: Pair<String, String> get() = Pair(firstText, secondText)

    // Generic function to retrieve the first image in the specified type R
    inline fun <reified R> getFirstImageAs(): R? {
        return firstImage?.getImageAs<R>() // Converts and returns the first image as type R, if available
    }

    // Generic function to retrieve the second image in the specified type R
    inline fun <reified R> getSecondImageAs(): R? {
        return secondImage?.getImageAs<R>() // Converts and returns the second image as type R, if available
    }
}
