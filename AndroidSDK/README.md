# AndroidSDK
需要在引用的项目根目录的build.gradle中添加一下 统一版本控制

ext {

    // Sdk and tools
    targetSdkVersion = 28
    compileSdkVersion = 28
    buildToolsVersion = '28.0.0'
    supportLibraryVersion = '28.0.0'
    
}


在应用地方 引用  rootProject.ext.targetSdkVersion
