/*
 * This file was generated by the Gradle 'init' task.
 */

val ktorVersion: String by rootProject
val koinVersion: String by rootProject

plugins {
    id("kotlin-application-conventions")
}

dependencies {
    api(project(":app:adapters:env"))
    api(project(":app:adapters:persist"))
    api(project(":app:adapters:remoting"))
    api(project(":app:adapters:primary-web"))
    api(project(":app:adapters:security"))
    api(project(":app:core"))

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
}

application {
    // Define the main class for the application.
    mainClass.set("infra.AppKt")
}
