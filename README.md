# vj1229Framework
A Game Framework for Android used in the [VJ1229](https://ujiapps.uji.es/sia/rest/publicacion/2020/estudio/231/asignatura/VJ1229) subject in www.uji.es

[![Release](https://jitpack.io/v/com.github.jvilar/vj1229framework.svg)](https://jitpack.io/#com.github.jvilar/vj1229framework)

## Installation

To use it in your projects, if the `build.gradle` of the project has an `allprojects`section, add to it the following:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

In recent versions of Android Studio, you have to add the line to the `settings.gradle` file:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url 'https://jitpack.io'}
    }
}
```

And add to the `build.gradle` of your app:

```
dependencies {
     ...
     implementation 'com.github.jvilar:vj1229Framework:v2021.1'
}
```

# Usage

Create an `Activity` by inheriting from |GameActivity|. 

