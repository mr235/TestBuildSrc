
plugins {
    `maven-publish`
    `kotlin-dsl`
//    kotlin("jvm") version "2.1.21"
}

//repositories {
//    google()
//    mavenCentral()
//}

// ./gradlew :bus-gradle-plugin:publish 发布插件
//group = "com.blankj" // 关键，防止使用默认 project.name

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.blankj"
            artifactId = "bus-agp8"
            version = "1.0.0"
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("../repo")
        }
    }
}


// 避免自动生成其他 publications 被发布
tasks.withType<PublishToMavenRepository>().configureEach {
    onlyIf { publication.name == "maven" }
}

dependencies {
    implementation(gradleApi())

    implementation("com.android.tools.build:gradle:8.10.1")
    implementation("com.android.tools.build:gradle-api:8.10.1")

    implementation("commons-io:commons-io:2.13.0")
    implementation("commons-codec:commons-codec:1.15")

    implementation("org.ow2.asm:asm-commons:9.6")
    implementation("org.ow2.asm:asm-util:9.7")
    implementation("org.ow2.asm:asm-tree:9.7")

    implementation("com.google.code.gson:gson:2.10.1")

}