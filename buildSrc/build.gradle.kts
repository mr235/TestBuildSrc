
plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.8.10"
}

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("TestPlugin") {
            id = "com.mr235.test"
            implementationClass = "com.mr235.test.TestPlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())

    implementation("com.android.tools.build:gradle:7.4.2")
    implementation("com.android.tools.build:gradle-api:7.4.2")

    implementation("commons-io:commons-io:2.13.0")
    implementation("commons-codec:commons-codec:1.15")

    implementation("org.ow2.asm:asm-commons:9.6")
    implementation("org.ow2.asm:asm-util:9.7")
    implementation("org.ow2.asm:asm-tree:9.7")

}