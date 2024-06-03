plugins {
    id("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.tfg.gestiondetareas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tfg.gestiondetareas"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.firebase:firebase-analytics")
    //Sistema de autentificación
    implementation("com.google.firebase:firebase-auth")
    //Para la base de datos
    implementation("com.google.firebase:firebase-database")
    implementation ("com.google.android.material:material:1.5.0")
    implementation(libs.preference)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}