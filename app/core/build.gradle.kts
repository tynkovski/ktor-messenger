val ktorVersion: String by rootProject

plugins {
    id("kotlin-library-conventions")
}

dependencies {
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
}
