package com.mr235.test.scanall

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

internal abstract class RouterClassesTask : DefaultTask() {

    @get:Incremental
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val jars: ListProperty<RegularFile>

    @get:Incremental
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val dirs: ListProperty<Directory>

    @get:OutputFile
    abstract val output: RegularFileProperty

    @TaskAction
    fun taskAction(inputChanges: InputChanges) {
        println("=====================RouterClassesTask========")

        val addOperateList = mutableListOf<String>()

        var needOperateByteArray: ByteArray? = null



        JarOutputStream(FileOutputStream(output.get().asFile)).use { jarOutput ->
            processTargetJars(jarOutput,addOperateList,){
                needOperateByteArray = it
            }

            processDirs(jarOutput,addOperateList)

            //获取所有需要被注册的类
            addOperateList.forEach {
            }
            //这行代码先在jarOutput中创建一个名为jarEntry.name的新条目
            //  新条目 com/example/base_arouter/ARouterUtils.class
            // 该条目在之前的操作processTargetJars（）方法中，跳过了
//            jarOutput.putNextEntry(JarEntry(PluginTools.REGISTER_CLASS_FILE_NAME))
//            //拿到之前保存的要操作的字节码文件
//            val input= ByteArrayInputStream(needOperateByteArray)
//            val reader = ClassReader(input)
//            val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
//            val classVisitor = RouterAllCassVisitor(classWriter,addOperateList)
//            reader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
//            jarOutput.write(classWriter.toByteArray())
//            input.use { inputStream ->
//                inputStream.copyTo(jarOutput)
//            }
            jarOutput.closeEntry()


        }


    }

    private fun processDirs(jarOutput: JarOutputStream, addOperateList: MutableList<String>) {
        dirs.get().forEach { directory ->
            directory.asFile.walk().forEach { file ->
                if (file.isFile) {
//                    println("tgw3 jar file: ${file.name}")

                    val relativePath = directory.asFile.toURI().relativize(file.toURI()).path
                    println("=======jarEntry=======processDirs : ${relativePath}")
//                    println("tgw4 jar file relativePath: ${relativePath}")

                    jarOutput.putNextEntry(JarEntry(relativePath.replace(File.separatorChar, '/')))

//                    println("tgw6 jar file relativePath replace : ${relativePath.replace(File.separatorChar, '/')}")
//                    if (PluginTools.isTargetProxyClass(relativePath?:"")) {
//                        println("tgw7 jar file isTargetProxyClass: ${relativePath}")
//
//                        addOperateList.add(relativePath?:"")
//                    }
                    file.inputStream().use { inputStream ->
                        inputStream.copyTo(jarOutput)
                    }
                    jarOutput.closeEntry()
                }
            }
        }
    }


    /**
     *jarOutput.putNextEntry(JarEntry(jarEntry.name))
     *
     * jarFile.getInputStream(jarEntry).use {
     *     it.copyTo(jarOutput)
     * }
     * 解释
     *1. 使用jarOutput.putNextEntry创建一个和输入jarEntry同名的新入口到输出jar中。
     * 2. 使用jarFile.getInputStream获取输入jarEntry对应的输入流。
     * 3. 使用InputStream的copyTo方法将输入流复制到JarOutputStream中,完成内容的复制。
     * 4. 用use方法保证流被关闭。
     * 这样,就实现了从输入的JarFile选择性地复制jarEntry到输出的jar文件中。
     */
    private fun processTargetJars(
        jarOutput: JarOutputStream,
        addOperateList: MutableList<String>,
        needOperateByteArrayListener:(it:ByteArray) -> Unit
    ) {
        jars.get().forEach { file ->
            JarFile(file.asFile).use { jarFile ->
                jarFile.entries().iterator().forEach { jarEntry ->
                    println("=======jarEntry=======processTargetJars jarEntry.name: ${jarEntry.name}")
                    //找到目标要 操作的 class文件 ，我这里是：com/example/base_arouter/ARouterUtils.class 文件
//                    if (!jarEntry.isDirectory && jarEntry.name.contains(PluginTools.REGISTER_CLASS_FILE_NAME)) {
//                        println("tgw101 jar target: ${jarEntry.name}")
//                        jarFile.getInputStream(jarEntry).use {
//                            // 获取所要操作文件的字节流，后面构建新的文件进行操作
//                            needOperateByteArrayListener.invoke(it.readAllBytes())
//                        }
//                    } else {
                        runCatching {
//                            println("tgw21 jar : ${jarEntry.name}")
//                            if (PluginTools.isTargetProxyClass(jarEntry.name?:"")) {
//                                addOperateList.add(jarEntry.name?:"")
//                            }
                            jarOutput.putNextEntry(JarEntry(jarEntry.name))

                            jarFile.getInputStream(jarEntry).use {
                                it.copyTo(jarOutput)
                            }
                        }/*.onFailure { e ->
                            Log.e("Copy jar entry failed. [entry:${jarEntry.name}]", e)
                        }*/
                        jarOutput.closeEntry()

//                    }
                }
            }
        }
    }
}