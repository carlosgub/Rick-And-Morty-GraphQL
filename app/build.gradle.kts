plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.apollographql.apollo3").version("3.8.0")
}

val composeUiVersion by extra { "1.3.3" }

@Suppress("UnstableApiUsage")
android {
    namespace = "com.carlosgub.rmgraphql"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.carlosgub.rmgraphql"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configurations {
        all {
            resolutionStrategy {
                exclude(group = "org.jetbrains.kotlin",module = "kotlin-stdlib-jdk7")
            }
        }
    }
    kotlinOptions {
        val experimentalOptIns = listOf(
            "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi",
            "-Xopt-in=androidx.compose.ui.graphics.ExperimentalGraphicsApi",
            "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )

        freeCompilerArgs =
            freeCompilerArgs + experimentalOptIns

        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"API_KEY\"")
        }

        release {
            buildConfigField("String", "API_KEY", "\"API_KEY\"")
        }
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))


    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.compose.material:material:1.4.1")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")

    //Compose
    implementation("androidx.compose.ui:ui-util:1.4.1")
    implementation("androidx.compose.material:material-icons-extended:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation("androidx.compose.ui:ui:$composeUiVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")

    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.43.2")

    //Apollo
    implementation("com.apollographql.apollo3:apollo-runtime:3.8.0")

    //Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    //Livedata
    implementation("androidx.compose.runtime:runtime-livedata:1.4.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

}
apollo {
    service("service") {
        packageName.set("com.carlosgub.rmgraphql")
    }
}