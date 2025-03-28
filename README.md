# DynamicTitleView
[![](https://jitpack.io/v/joelnah/DynamicTitleView.svg)](https://jitpack.io/#joelnah/DynamicTitleView)

### Setting

```kotlin
//settings.gradle
maven(url = "https://jitpack.io")
//build.gradle
implementation ("com.github.joelnah:DynamicTitleView:Tag")
```

## Usage

### Application
```kotlin
val scrollState = rememberScrollState() // or rememberLazyListState()

DynamicTitleView(
    scrollState = scrollState,
    data = TitleData( //It can also be used partially.
        firstImage = ImageData(Icons.Default.Call),
        secondImage = ImageData(Icons.Default.Info),
        firstText = "Hello",
        secondText = "Information",
    ),
)

// if rememberScrollState is used
Column(
    modifier = Modifier.verticalScroll(scrollState),
) { }

// if rememberLazyListState is used
LazyColumn(
    state = scrollState
) {  }
```
