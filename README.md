# MCTweakerDevEnvFix
A simple fix for tweaker not loading in some MC dev env

[![JitPack State](https://jitpack.io/v/Water-OR/MCTweakerDevEnvFix.svg)](https://jitpack.io/#Water-OR/MCTweakerDevEnvFix)

## Add following to your `build.gradle.kts` to activate this
```gradle
repositories {
    maven("https://jitpack.io") {
        name = "Jitpack Repo"
        content {
            includeGroupByRegex("com\\.github\\.(.)+")
        }
    }
}

dependencies {
    runtimeOnly("com.github:Water-OR:MCTweakerDevEnvFix:1.0")
}
```

> [!NOTE]
> You can disable it by setting `mcTweakerDevEnvFix.disabled` to `false`
