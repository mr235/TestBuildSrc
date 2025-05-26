package com.blankj_custom.bus

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.blankj_custom.base_transform.util.LogUtils
import org.gradle.api.Plugin
import org.gradle.api.Project

class BusPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        LogUtils.init(target)
        println("==================== Plugin ====================BusPlugin AGP8.0")
        target.extensions.findByType(AndroidComponentsExtension::class.java)?.apply {
            onVariants { variant ->

                val taskProviderTransformAllClassesTask =
                    target.tasks.register(
                        "${variant.name}BusClassesTask",
                        BusClassesTask::class.java
                    )
                variant.artifacts.forScope(ScopedArtifacts.Scope.ALL)
                    .use(taskProviderTransformAllClassesTask)
                    .toTransform(
                        ScopedArtifact.CLASSES,
                        BusClassesTask::jars,
                        BusClassesTask::dirs,
                        BusClassesTask::output
                    )
            }
        }

    }
}