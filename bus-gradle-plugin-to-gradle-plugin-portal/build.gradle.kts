
plugins {
    // 发布到gradle插件中心
    id("com.gradle.plugin-publish") version "1.3.1"
    `maven-publish`
    `kotlin-dsl`
}

version = "1.0.0"
group = "com.blankj"

gradlePlugin {
    website.set("https://github.com/mr235/TestBuildSrc/tree/agp8.0")
    vcsUrl.set("https://github.com/mr235/TestBuildSrc/tree/agp8.0")
    plugins {
        create("busPlugin") {
            id = "com.blankj.bus-agp8"
            implementationClass = "com.blankj_custom.bus.BusPlugin"
            displayName = "BusPlugin for AGP8.0"
            description = "BusPlugin for AGP8.0"
            tags.set(listOf("android", "bus", "agp8", "gradle-plugin"))
        }
    }
}

// 以下是发布到本地maven仓库
// ./gradlew :bus-gradle-plugin:publish 发布插件
//group = "com.blankj" // 关键，防止使用默认 project.name

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = "com.blankj"
//            artifactId = "bus-agp8"
//            version = "1.0.0"
//            from(components["java"])
//        }
//    }
//    repositories {
//        maven {
//            url = uri("../repo")
//        }
//    }
//}


// 避免自动生成其他 publications 被发布
//tasks.withType<PublishToMavenRepository>().configureEach {
//    onlyIf { publication.name == "maven" }
//}

tasks.withType<JavaCompile>().configureEach {
    // 移除或注释掉会开启所有警告的行，例如：
    // options.compilerArgs.add("-Xlint:all")

    // 如果你只想禁用 Javadoc 相关的警告，可以明确添加以下行：
//    options.compilerArgs.add("-Xlint:all,-missing-javadoc") // 禁用缺少 Javadoc 的警告
    options.compilerArgs.add("-Xlint:-serial")         // 禁用关于 serialVersionUID 的警告 (常见)
    options.compilerArgs.add("-Xlint:-processing")     // 禁用与注解处理器相关的警告
    // 更多 Xlint 选项可以查阅 Javac 文档
}


dependencies {
    implementation(gradleApi())

    implementation("com.android.tools.build:gradle:8.10.1")
    implementation("com.android.tools.build:gradle-api:8.10.1")

    implementation("commons-io:commons-io:2.16.1")
    implementation("commons-codec:commons-codec:1.15")

    implementation("org.ow2.asm:asm-commons:9.7.1")
    implementation("org.ow2.asm:asm-util:9.7.1")
    implementation("org.ow2.asm:asm-tree:9.7.1")

    implementation("com.google.code.gson:gson:2.11.0")

}