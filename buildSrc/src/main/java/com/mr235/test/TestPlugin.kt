package com.mr235.test

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationParameters
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.mr235.test.scanall.RouterClassesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor

class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("==================== Plugin ====================TestPlugin")

        target.extensions.findByType(AndroidComponentsExtension::class.java)?.apply {
            onVariants { variant ->
                variant.instrumentation.transformClassesWith(
                    MyClassVisitorFactory::class.java,
                    InstrumentationScope.ALL
                ) {
                    // 配置传递参数（如果需要）
                }
//                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_CLASSES)
//                instrumentation. excludes. add("com`/`example`/`donotinstrument`/`**")
//                instrumentation. excludes. add("**`/`*Test")


                val taskProviderTransformAllClassesTask =
                    target.tasks.register(
                        "${variant.name}RouterClassesTask",
                        RouterClassesTask::class.java
                    )
                // https://github.com/android/gradle-recipes
                variant.artifacts.forScope(ScopedArtifacts.Scope.ALL)
                    .use(taskProviderTransformAllClassesTask)
                    .toTransform(
                        ScopedArtifact.CLASSES,
                        RouterClassesTask::jars,
                        RouterClassesTask::dirs,
                        RouterClassesTask::output
                    )
            }
        }
    }


    abstract class MyClassVisitorFactory :
        AsmClassVisitorFactory<InstrumentationParameters.None> {

        override fun createClassVisitor(
            classContext: ClassContext,
            nextClassVisitor: ClassVisitor,
        ): ClassVisitor {
            return PageClassVisitor(nextClassVisitor)
        }

        override fun isInstrumentable(classData: ClassData): Boolean {
            return true
        }
    }
}