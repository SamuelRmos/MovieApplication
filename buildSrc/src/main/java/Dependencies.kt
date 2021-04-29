package movieApplication.buildSrc

object Libs {

    object Android {
        const val appCompat = "androidx.appcompat:appcompat:1.2.0"
        const val androidXCore = "androidx.core:core-ktx:1.3.2"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val multiDex = "com.android.support:multidex:1.0.3"
    }

    object Moshi {
        private const val version = "1.8.0"
        const val moshi = "com.squareup.moshi:moshi-kotlin:$version"
        const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
    }

    object Retrofit {
        private const val version = "2.6.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val retrofitConverterMoshi = "com.squareup.retrofit2:converter-moshi:$version"
        const val retrofitCoroutines =
            "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
    }

    object OkHttp {
        private const val version = "4.9.0"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val interceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Kotlin {
        private const val version = "1.4.2"
        private const val kotlinVersion = "1.4.32"

        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object Google {
        private const val version = "1.4.0-alpha02"
        const val materialDesign = "com.google.android.material:material:$version"
    }

    object LiveData {
        private const val liveDataVersion = "2.3.1"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$liveDataVersion"
    }

    object Room {
        private const val version = "2.3.0"
        const val roomRunTime = "androidx.room:room-runtime:$version"
        const val roomCompiler = "androidx.room:room-compiler:$version"
    }

    object Hilt {
        private const val hiltVersion = "2.33-beta"
        private const val hiltAndroidXVersion = "1.0.0-alpha03"

        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
        const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltVersion"
        const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$hiltAndroidXVersion"
        const val hiltAndroidxCompiler = "androidx.hilt:hilt-compiler:$hiltAndroidXVersion"
    }

    object Glide {
        private const val version = "4.11.0"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val glideCompiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Test {
        private const val espressoVersion = "3.3.0"

        const val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"
        const val espressoContrib =  "androidx.test.espresso:espresso-contrib:$espressoVersion"
        const val testRules = "androidx.test:rules:1.4.0-alpha05"

        const val jUnit = "junit:junit:4.13.2"
        const val jUnitExt = "androidx.test.ext:junit:1.1.2"
        const val coreTesting = "android.arch.core:core-testing:1.1.1"
        const val mockServer = "com.squareup.okhttp3:mockwebserver:4.1.0"

        const val mockk = "io.mockk:mockk:1.9.3"
        const val mockito = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
        const val androidXTestCore = "androidx.test:core:1.3.0"
        const val androidXLegacy = "androidx.legacy:legacy-support-v4:1.0.0"
    }

    object Navigation {
        private const val version = "2.3.5"
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:$version"
        const val navigationUI = "androidx.navigation:navigation-ui-ktx:$version"
        const val navigationDynamic = "androidx.navigation:navigation-dynamic-features-fragment:$version"
        const val navigationPlugin = "android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0"
    }
}