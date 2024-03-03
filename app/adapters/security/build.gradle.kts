val ktorVersion: String by rootProject
val koinVersion: String by rootProject
val codecVersion: String by rootProject

plugins {
    id("kotlin-library-conventions")
    kotlin("plugin.serialization")
}

dependencies {
    api(project(":app:core"))
    api(project(":app:common"))
    implementation("commons-codec:commons-codec:$codecVersion")
}