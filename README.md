# core

latest_version : [![](https://jitpack.io/v/2math/core.svg)](https://jitpack.io/#2math/core)

To start, add in your project build.gradle file 
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }

    }
}
```

Next add as dependency in your application build.gradle

#Important from v1.1.5 the lib is migrated to AndroidX, in new branch androidX

```
implementation 'com.github.2math:core:latest_version'
//    required by com.github.2math:core lib
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
    implementation "com.google.code.gson:gson:${rootProject.ext.gson}"

    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.exifinterface:exifinterface:1.1.0'

    implementation 'com.squareup.picasso:picasso:2.5.2'
    implementation 'com.balysv:material-ripple:1.0.2'
    implementation ('co.infinum:materialdatetimepicker-support:3.5.1') {
        exclude group: 'com.android.support'
    }
//    required by com.github.2math:core lib end
```

#For v1.1.4 or earlier use :

```
implementation 'com.github.2math:core:latest_version'
//    required by com.github.2math:core lib
    implementation "android.arch.lifecycle:extensions:${rootProject.ext.lifecycle}"
    implementation "com.google.code.gson:gson:${rootProject.ext.gson}"

    implementation "com.android.support:appcompat-v7:${rootProject.ext.supportLibraryVersion}"
    implementation "com.android.support:support-v4:${rootProject.ext.supportLibraryVersion}"
    implementation "com.android.support:exifinterface:${rootProject.ext.supportLibraryVersion}"

    implementation 'com.squareup.picasso:picasso:2.5.2'
    implementation 'com.balysv:material-ripple:1.0.2'
    implementation ('co.infinum:materialdatetimepicker-support:3.5.1') {
        exclude group: 'com.android.support'
    }
//    required by com.github.2math:core lib end
//  this one is provided by com.github.2math:core lib : "com.android.support:design:28.0.0"
```

In your Application class init the library as in the example app :
https://github.com/2math/core/blob/master/app/src/main/java/com/criapp_studio/coreapp/AppApplication.java
