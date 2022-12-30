#!/usr/bin/env kotlin

/**
 * This script loads metadata of all public classes and type aliases from kotlin-stdlib and dumps their names
 * to stdout, ready to be used in the Vim syntax file.
 *
 * To run locally, change stdlib paths in STDLIB and STDLIB_COMMON variables below. You can download the latter
 * from Maven Central, or build it in the Kotlin project itself with `./gradlew dist`.
 */

@file:DependsOn("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")
@file:DependsOn("org.ow2.asm:asm:8.0.1")

import kotlinx.metadata.Flag
import kotlinx.metadata.KmClass
import kotlinx.metadata.KmTypeAlias
import kotlinx.metadata.internal.common.KotlinCommonMetadata
import kotlinx.metadata.isLocal
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.*
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import java.io.File
import java.io.InputStream
import java.util.zip.ZipFile

val STDLIB = File(System.getProperty("user.home") + "/kotlin/dist/kotlinc/lib/kotlin-stdlib.jar")
val STDLIB_COMMON = File(System.getProperty("user.home") + "/kotlin/libraries/stdlib/common/build/libs/kotlin-stdlib-common-1.8.255-SNAPSHOT.jar")

val DEBUG = false

val PREFIX = "syn keyword ktType"
val CUTOFF = 180

fun extractSimpleNameFromClass(inputStream: InputStream, entryName: String): String? {
    val reader = ClassReader(inputStream)

    var kind: Int? = null
    var metadataVersion: IntArray? = null
    var data1: Array<String>? = null
    var data2: Array<String>? = null
    var extraString: String? = null
    var packageName: String? = null
    var extraInt: Int? = null

    reader.accept(object : ClassVisitor(Opcodes.ASM8) {
        override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
            if (descriptor != "Lkotlin/Metadata;") return null

            return object : AnnotationVisitor(Opcodes.ASM8) {
                override fun visit(name: String?, value: Any?) {
                    when (name) {
                        "k" -> kind = value as Int
                        "mv" -> metadataVersion = value as IntArray
                        "xs" -> extraString = value as String
                        "pn" -> packageName = value as String
                        "xi" -> extraInt = value as Int
                    }
                }

                override fun visitArray(name: String?): AnnotationVisitor =
                    object : AnnotationVisitor(Opcodes.ASM8) {
                        val strings = mutableListOf<String>()

                        override fun visit(empty: String?, value: Any?) {
                            strings += value as String
                        }

                        override fun visitEnd() {
                            when (name) {
                                "d1" -> data1 = strings.toTypedArray()
                                "d2" -> data2 = strings.toTypedArray()
                            }
                        }
                    }
            }
        }
    }, SKIP_CODE or SKIP_DEBUG or SKIP_FRAMES)

    if (kind == null) return null

    val header = KotlinClassHeader(kind, metadataVersion, data1, data2, extraString, packageName, extraInt)
    when (val metadata = KotlinClassMetadata.read(header)) {
        is KotlinClassMetadata.Class -> {
            val klass = metadata.toKmClass()
            if (isPublicAPI(klass)) {
                val simpleName = klass.name.simpleName
                if (simpleName != "Companion") {
                    if (DEBUG) {
                        println("class ${klass.name} -> $simpleName")
                    }
                    return simpleName
                }
            }
        }
        null -> error("Unsupported Kotlin metadata: $entryName")
        else -> {}
    }

    return null
}

val String.simpleName: String
    get() = substringAfterLast('/').substringAfterLast('.')

fun extractSimpleNamesFromBuiltins(inputStream: InputStream, packageName: String): List<String> {
    val metadata = KotlinCommonMetadata.read(inputStream.use(InputStream::readAllBytes))
        ?: error("Unsupported Kotlin metadata: $packageName")
    val module = metadata.toKmModuleFragment()

    val classNames = module.classes
        .filter { isPublicAPI(it) }
        .map {
            val name = it.name
            if (DEBUG) {
                println("builtin class $name -> ${name.simpleName}")
            }
            name.simpleName
        }

    val typeAliasNames = module.pkg?.typeAliases.orEmpty()
        .filter { isPublicAPI(it) }
        .map {
            val name = it.name
            if (DEBUG) {
                println("builtin typealias $name -> ${name.simpleName}")
            }
            name.simpleName
        }

    // TODO: fix typealias loading and avoid kotlin-stdlib-common
    require(typeAliasNames.isEmpty()) { typeAliasNames }

    return classNames + typeAliasNames
}

fun isPublicAPI(klass: KmClass): Boolean {
    val name = klass.name
    return !name.isLocal &&
        isPublicAPIName(name) &&
        (Flag.IS_PUBLIC(klass.flags) || Flag.IS_PROTECTED(klass.flags))
}

fun isPublicAPI(typeAlias: KmTypeAlias): Boolean =
    isPublicAPIName(typeAlias.name) &&
        (Flag.IS_PUBLIC(typeAlias.flags) || Flag.IS_PROTECTED(typeAlias.flags)) &&
        // This is kind of a hack, but we don't want deprecated stuff to be used anyway.
        // This is only done for type aliases though, since for classes it's more difficult to load annotations.
        typeAlias.annotations.none {
            it.className == "kotlin/Deprecated"
        }

// These names are too common to be highlighted only on the basis that there are such classes in kotlin-stdlib.
val TOO_COMMON_NAMES = setOf("Companion", "Default")

fun isPublicAPIName(name: String): Boolean =
    "/internal/" !in name && name.simpleName !in TOO_COMMON_NAMES

fun collectClassNames(file: File): List<String> {
    val classNames = mutableListOf<String>()
    ZipFile(file).use { zipFile ->
        for (entry in zipFile.entries()) {
            when {
                entry.name.endsWith(".class") -> {
                    extractSimpleNameFromClass(zipFile.getInputStream(entry), entry.name)?.let { simpleName ->
                        classNames += simpleName
                    }
                }
                entry.name.endsWith(".kotlin_builtins") || entry.name.endsWith(".kotlin_metadata") -> {
                    classNames += extractSimpleNamesFromBuiltins(
                        zipFile.getInputStream(entry),
                        entry.name.substringBeforeLast('/')
                    )
                }
            }
        }
    }
    return classNames
}

fun outputClassNames(unsortedNames: List<String>) {
    val names = HashSet(unsortedNames).sorted()
    var current = StringBuilder(PREFIX)
    for (name in names) {
        if (current.length + 1 + name.length > CUTOFF) {
            println(current)
            current = StringBuilder(PREFIX)
        }
        current.append(" ").append(name)
    }

    if (current.length > PREFIX.length) {
        println(current)
    }
}

fun main() {
    val names = collectClassNames(STDLIB) + collectClassNames(STDLIB_COMMON)
    outputClassNames(names)
}

main()
