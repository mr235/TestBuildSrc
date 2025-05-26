package com.blankj_custom.bus

import com.blankj_custom.base_transform.util.JsonUtils
import com.blankj_custom.base_transform.util.LogUtils
import org.apache.commons.io.FileUtils
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
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.regex.Pattern

internal abstract class BusClassesTask : DefaultTask() {

    private var busUtilsClass: String? = null
    private var jsonFile: File? = null
    private val busMap: MutableMap<String, List<BusInfo>> = mutableMapOf()
    private var busUtilsFile: File? = null
    private var busUtilsJarFile: JarFile? = null
    private var busUtilsJarEntry: JarEntry? = null


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
    fun taskAction() {
        project.extensions.create(getPluginName(), BusExtension::class.java)
        onScanStarted()


        val jarOutput = JarOutputStream(
            BufferedOutputStream(
                FileOutputStream(
                    output.get().asFile
                )
            )
        )
        jars.get().forEach { file ->
            val jarFile = JarFile(file.asFile)
            var isIgnore = false
            jarFile.entries().iterator().forEach { jarEntry ->
                if (jarEntry.name.endsWith(".class") && !jarEntry.name.contains("META-INF")) {
                    val clazz = jarEntry.name.replace(File.separatorChar, '.')
                        .substring(0, jarEntry.name.length - 6)
                    if (clazz != getExt().busUtilsClass) {
                        jarOutput.putNextEntry(JarEntry(jarEntry.name))
                        jarFile.getInputStream(jarEntry).use {
                            it.copyTo(jarOutput)
                        }
                        jarOutput.closeEntry()
                        if (!isIgnore) {
                            isIgnore = isIgnoreScan(jarEntry.name.replace('/', '.'))
                        }
                        if (!isIgnore) {
                            jarFile.getInputStream(jarEntry).use {
                                scanClassFile(it)
                            }
                        }
                    } else {
                        busUtilsJarFile = JarFile(file.asFile)
                        busUtilsJarEntry = JarEntry(jarEntry.name)
                    }

                }
            }
            jarFile.close()
        }
        dirs.get().forEach { directory ->
            directory.asFile.walk().forEach { file ->
                if (file.isFile) {

                    val relativePath = directory.asFile.toURI().relativize(file.toURI()).path
                    val clazz = relativePath.replace(File.separatorChar, '.')
                        .substring(0, relativePath.length - 6)
                    if (clazz != getExt().busUtilsClass) {
                        jarOutput.putNextEntry(
                            JarEntry(
                                relativePath.replace(
                                    File.separatorChar,
                                    '/'
                                )
                            )
                        )

                        file.inputStream().use { inputStream ->
                            inputStream.copyTo(jarOutput)
                        }
                        if (file.absolutePath.endsWith(".class")) {
                            file.inputStream().use {
                                scanClassFile(it)
                            }
                        }
                        jarOutput.closeEntry()

                    } else {
                        busUtilsFile = file
                        busUtilsJarEntry = JarEntry(relativePath.replace(File.separatorChar, '/'))
                    }

                }
            }
        }
        onScanFinished(jarOutput)
        jarOutput.close()
    }

    private fun onScanStarted() {
        busUtilsClass = getExt().busUtilsClass
        if (busUtilsClass?.trim().equals("")) {
            throw Exception("BusExtension's busUtilsClass is empty.")
        }
        jsonFile = File(project.projectDir.absolutePath, "__bus__.json")
        FileUtils.write(jsonFile, "{}", Charset.defaultCharset())
    }

    private fun onScanFinished(jarOutput: JarOutputStream) {
        var busUtilsClassInputStream: InputStream? = null
        if (busUtilsFile != null) {
            busUtilsClassInputStream = busUtilsFile?.inputStream()
        }
        if (busUtilsJarFile != null && busUtilsJarEntry != null) {
            busUtilsClassInputStream = busUtilsJarFile?.getInputStream(busUtilsJarEntry)
        }

        if (busUtilsClassInputStream == null) {
            return
        }
        if (busMap.isEmpty()) {
            println("no bus.")
            return
        }
        busMap.forEach { (tag, infoList) ->
            infoList.sortedWith(Comparator { o1, o2 ->
                o1.priority - o2.priority
            })
        }

        val rightBus = mutableMapOf<String, List<String>>()
        val wrongBus = mutableMapOf<String, List<String>>()

        busMap.forEach { (tag, infoList) ->
            val rightInfoString = mutableListOf<String>()
            val wrongInfoString = mutableListOf<String>()
            infoList.forEach {
                if (it.isParamSizeNoMoreThanOne) {
                    rightInfoString.add(it.toString())
                } else {
                    wrongInfoString.add(it.toString())
                }
            }
            if (rightInfoString.isNotEmpty()) {
                rightBus[tag] = rightInfoString
            }
            if (wrongInfoString.isNotEmpty()) {
                wrongBus[tag] = wrongInfoString
            }
        }
        val busDetails = mutableMapOf<String, Any>()
        busDetails.put("BusUtilsClass", getExt().busUtilsClass)
        busDetails.put("rightBus", rightBus)
        busDetails.put("wrongBus", wrongBus)
        val busJson = JsonUtils.getFormatJson(busDetails)
        log(jsonFile.toString() + ": " + busJson)
        FileUtils.write(jsonFile, busJson)

        try {

            busUtilsClassInputStream.available()
            busUtilsClassInputStream.use { stream ->
                busUtilsJarEntry?.let {
                    jarOutput.putNextEntry(it)
                    val cr = ClassReader(stream)
                    val cw = ClassWriter(cr, ClassWriter.COMPUTE_FRAMES)
                    val cv = BusUtilsClassVisitor(cw, busMap, busUtilsClass);
                    cr.accept(cv, ClassReader.SKIP_FRAMES);
                    cw.toByteArray().inputStream().use {
                        it.copyTo(jarOutput)
                    }
                }
            }
            jarOutput.closeEntry()
            busUtilsJarFile?.close()
            busUtilsClassInputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getExt(): BusExtension {
        return project.extensions.getByName(getPluginName()) as BusExtension
    }

    private fun getPluginName(): String {
        return Config.EXT_NAME
    }

    fun isIgnoreScan(jarName: String): Boolean {
        if (jarName.contains("utilcode")) {
            return false
        }

        if (getExt().onlyScanLibRegex.isNotBlank()) {
            return !Pattern.matches(getExt().onlyScanLibRegex, jarName)
        }

        if (getExt().jumpScanLibRegex.isNotBlank()) {
            if (Pattern.matches(getExt().jumpScanLibRegex, jarName)) {
                return true
            }
        }

        if (Config.EXCLUDE_LIBS_START_WITH.any { jarName.startsWith(it) }) {
            return true
        }

        return false
    }

    private fun scanClassFile(classStream: InputStream) {
        val cr = ClassReader(classStream)
        val cw = ClassWriter(cr, 0)
        try {
            val cv = BusClassVisitor(cw, busMap, busUtilsClass)
            cr.accept(cv, ClassReader.EXPAND_FRAMES)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    private fun log(obj: Any) {
        LogUtils.l(getPluginName(), obj)
    }

}