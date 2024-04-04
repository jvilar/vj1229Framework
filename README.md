# vj1229Framework
A Game Framework for Android used in the [VJ1229](https://ujiapps.uji.es/sia/rest/publicacion/2020/estudio/231/asignatura/VJ1229) subject in www.uji.es

[![Release](https://jitpack.io/v/com.github.jvilar/vj1229framework.svg)](https://jitpack.io/#com.github.jvilar/vj1229framework)

## Installation

If you have a `settings.gradle` file (not `settings.gradle.kts`), add the Maven repository:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url 'https://jitpack.io'}
    }
}
```

If you have a `settings.gradle.kts`, add the Maven repository:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven("https://jitpack.io")
    }
}
```

And add to the `build.gradle` of your app:

```
dependencies {
     ...
     implementation 'com.github.jvilar:vj1229Framework:v2024.1'
}
```

Or, if you have a `build.gradle.kts`:

```
dependencies {
    ...
    implementation("com.github.jvilar:vj1229Framework:v2024.1")
}
```

# Usage

Create an `Activity` by inheriting from `GameActivity`. This `Activity` holds
a `GameView`, which is a `View` that inherits from `SurfaceView`. The `GameView`
repeatedly calls an `IEventProcessor` to notify the touch events and the time
from call to call and also calls an `IBitmapProvider` to obtain the new image to
display.

## The `GameActivity`

### `onCreate`

The `onCreate` method of the `GameActivity` must take the steps to guarantee that
there is a `GameView` available. If that `GameView` is part of a layout inflated
by the `onCreate` method, it must be returned by `getGameView`.

If the `GameView` is the only view and the orientation of the game is fixed, it
is possible to use either `portraitFullScreenOnCreate` or `landscapeFullScreenOnCreate`
to take care of the details. This way, the complete `onCreate` function can be simply

```
  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        landscapeFullScreenOnCreate()
    }
```

### Measures

The `GameView` will call the `onBitmapMeasuresAvailable` function when it is assigned
screen space. This can be used to scale the graphic resources.

### Update Render Loop

The `GameView` will call the method `onUpdate` to notify the time in seconds since the
last call to this method and the list of touch events since the last call. This method
is in the interface `IEventProcessor` and the `GameView` calls the method `getEventProcessor`
of the `GameActivity` to know which object can be used.

After the call of `onUpdate`, the `GameView` calls the `onDrawingRequested` to get the `Bitmap`
that will be displayed.

# The Helper Classes for Graphics

The framework has three classes to help in the creation of graphics: Graphics, AnimatedBitmap, and SpriteSheet.

## Graphics

This is a thin wrapper around a 'Bitmap'. It offers several functions to draw over the `Bitmap` like:
`clear`, `drawLine`, `drawBitmap`, drawText, ...
The current state of the `Bitmap` is recovered with `getFrameBuffer`.

## AnimatedBitmap

This class stores several bitmaps corresponding to the frames of an animation and keeps a timer.
Calling `onUpdate` increases the timer and updates the frame if needed. The current frame is
recoverd using `getCurrentFrame`.

## SpriteSheet

This class treats a `Bitmap` as an array of smaller images (*sprites*). The images can be recovered using
`getScaledRow`, `getSprite`, or `getScaledSprite`.

# Further Documentation

The complete documentation is available [here](https://www3.uji.es/~jvilar/vj1229Framework).
