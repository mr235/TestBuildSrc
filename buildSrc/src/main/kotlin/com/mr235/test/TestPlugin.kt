package com.mr235.test

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationParameters
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
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
                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_CLASSES)
                variant.instrumentation.excludes.add("**/BuildConfig")
                variant.instrumentation.excludes.add("androidx/appcompat/widget/**")
                variant.instrumentation.excludes.add("com/google/android/material/**")
                variant.instrumentation.excludes.add("androidx/appcompat/view/**")
                variant.instrumentation.excludes.add("kotlin/**")
                variant.instrumentation.excludes.add("kotlinx/**")
                variant.instrumentation.excludes.add("**/R$**")
                variant.instrumentation.excludes.add("**/R")
                variant.instrumentation.excludes.add("android/support/v4/**")
                variant.instrumentation.excludes.add("androidx/activity/**")
                variant.instrumentation.excludes.add("androidx/annotation/**")
                variant.instrumentation.excludes.add("androidx/appcompat/**")
                variant.instrumentation.excludes.add("androidx/arch/**")
                variant.instrumentation.excludes.add("androidx/cardview/**")
                variant.instrumentation.excludes.add("androidx/collection/**")
                variant.instrumentation.excludes.add("androidx/concurrent/**")
                variant.instrumentation.excludes.add("androidx/constraintlayout/**")
                variant.instrumentation.excludes.add("androidx/coordinatorlayout/**")
                variant.instrumentation.excludes.add("androidx/core/**")
                variant.instrumentation.excludes.add("androidx/cursoradapter/**")
                variant.instrumentation.excludes.add("androidx/customview/**")
                variant.instrumentation.excludes.add("androidx/documentfile/**")
                variant.instrumentation.excludes.add("androidx/drawerlayout/**")
                variant.instrumentation.excludes.add("androidx/dynamicanimation/**")
                variant.instrumentation.excludes.add("androidx/fragment/**")
                variant.instrumentation.excludes.add("androidx/interpolator/**")
                variant.instrumentation.excludes.add("androidx/lifecycle/**")
                variant.instrumentation.excludes.add("androidx/loader/**")
                variant.instrumentation.excludes.add("androidx/localbroadcastmanager/**")
                variant.instrumentation.excludes.add("androidx/print/**")
                variant.instrumentation.excludes.add("androidx/recyclerview/**")
                variant.instrumentation.excludes.add("androidx/savedstate/**")
                variant.instrumentation.excludes.add("androidx/tracing/**")
                variant.instrumentation.excludes.add("androidx/transition/**")
                variant.instrumentation.excludes.add("androidx/vectordrawable/**")
                variant.instrumentation.excludes.add("androidx/versionedparcelable/**")
                variant.instrumentation.excludes.add("androidx/viewpager/**")
                variant.instrumentation.excludes.add("androidx/viewpager2/**")
                variant.instrumentation.excludes.add("com/google/**")
                variant.instrumentation.excludes.add("org/intellij/**")
                variant.instrumentation.excludes.add("org/jetbrains/**")
//                instrumentation. excludes. add("com`/`example`/`donotinstrument`/`**")
//                instrumentation. excludes. add("**`/`*Test")
            }
        }
    }


    abstract class MyClassVisitorFactory :
        AsmClassVisitorFactory<InstrumentationParameters.None> {

        override fun createClassVisitor(
            classContext: ClassContext,
            nextClassVisitor: ClassVisitor
        ): ClassVisitor {
            println("==================== Plugin ====================isInstrumentable ${classContext.currentClassData.className}")
            return PageClassVisitor(nextClassVisitor)
        }

        override fun isInstrumentable(classData: ClassData): Boolean {
//            println("==================== Plugin ====================isInstrumentable ${classData.className}")
            return true
        }
    }
}