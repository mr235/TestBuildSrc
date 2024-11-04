package com.mr235.test


import com.android.build.gradle.AppExtension
import com.android.build.gradle.TestedExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        println "==================== Plugin ====================TestPlugin"

        TestedExtension extension = project.extensions.getByType(AppExtension)

        extension.registerTransform(new MyTransform())
    }
}