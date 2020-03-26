# RoamChat-Kit
RoamChat library with emalify.
## First time integration 
#### Quick Steps
Project gradle file **build.gradle**
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

App Module gradle file **(app/build.gradle)** 
```
dependencies {
     implementation 'com.github.emalify-oss:RoamChat-Kit:{latestVersion}'
}
```

## Updating to newer versions of SDK
### [Changelog](https://github.com/emalify-oss/RoamChat-Kit/blob/master/CHANGELOG.mb)
