# vj1229Framework
A Game Framework for Android used in the [VJ1229](https://ujiapps.uji.es/sia/rest/publicacion/2020/estudio/231/asignatura/VJ1229) subject in www.uji.es

[![Release](https://jitpack.io/v/com.github.jvilar/vj1229framework.svg)](https://jitpack.io/#com.github.jvilar/vj1229framework)

## Installation

If you have a `settings.gradle` file (not `settings.gradle`), add the maven repository:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url 'https://jitpack.io'}
    }
}
```

If you have a `settings.gradle.kts`, add the maven repository:

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

Create an `Activity` by inheriting from |GameActivity|. 

