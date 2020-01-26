import com.beust.klaxon.Klaxon
import com.squareup.kotlinpoet.*
import java.io.File
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

const val GODOT_API_PATH = "godot_api.json"
const val GENERATED_PATH = "../../wrapper/godot-library/src/main/kotlin/godot/generated/"

fun main() {
    val classes: List<Class> = Klaxon().parseArray(File(GODOT_API_PATH).readText())!!

    val tree = classes.buildTree()
    val icalls = mutableSetOf<ICall>()

    for (cl in classes) cl.generate(GENERATED_PATH, tree, icalls)

    val iCallFileSpec = FileSpec.builder("godot.icalls", "__icalls")
    icalls.forEach { iCallFileSpec.addFunction(it.iCallSpec) }
    val variantMember = MemberName("godot.core", "Variant")
    iCallFileSpec.addFunction(
            FunSpec.builder("_icall_varargs").addModifiers(KModifier.INTERNAL).returns(ClassName("godot.core", "Variant"))
                    .addParameter("mb",
                            ClassName("kotlinx.cinterop", "CPointer")
                            .parameterizedBy(ClassName("godot.gdnative", "godot_method_bind"))
                    )
                    .addParameter("inst", ClassName("kotlinx.cinterop", "COpaquePointer"))
                    .addParameter("arguments", ClassName("kotlin", "Array").parameterizedBy(STAR))
                    .addStatement("""%M {
                            |    val args = %M<%T<%M>>(arguments.size)
                            |    for ((i,arg) in arguments.withIndex()) args[i] = %N.from(arg).nativeValue.ptr
                            |    val result = %M(mb, inst, args, arguments.size, null)
                            |    for (i in arguments.indices) %M(args[i])
                            |    return %N(result)
                            |}
                            |""".trimMargin(),
                            MemberName("kotlinx.cinterop","memScoped"),
                            MemberName("kotlinx.cinterop", "allocArray"),
                            ClassName("kotlinx.cinterop", "CPointerVar"),
                            MemberName("godot.gdnative", "godot_variant"),
                            variantMember,
                            MemberName("godot.gdnative", "godot_method_bind_call"),
                            MemberName("godot.gdnative", "godot_variant_destroy"),
                            variantMember)
                    .build()
    )
    val icallsFile = File(GENERATED_PATH)
    icallsFile.parentFile.mkdirs()
    iCallFileSpec.addImport("kotlinx.cinterop", "set", "get")
            .addImport("godot.core", "getRawMemory")
            .addImport("godot.core", "String").build().writeTo(icallsFile)


//    icallsFile.writeText(buildString {
//        appendln("@file:Suppress(\"unused\", \"ClassName\", \"EnumEntryName\", \"FunctionName\", \"SpellCheckingInspection\", \"PackageDirectoryMismatch\", \"RedundantExplicitType\")")
//        appendln("package godot.icalls")
//        appendln()
//        appendln("import godot.gdnative.*")
//        appendln("import godot.core.*")
//        appendln("import godot.*")
//        appendln("import kotlinx.cinterop.*")
//        appendln()
//        appendln()
//
//
//        appendln("""
//internal fun _icall_varargs(mb: CPointer<godot_method_bind>, inst: COpaquePointer, arguments: Array<*>): Variant {
//    memScoped {
//        val args = allocArray<CPointerVar<godot_variant>>(arguments.size)
//        for ((i,arg) in arguments.withIndex())
//            args[i] = Variant.from(arg).nativeValue.ptr
//        val result = godot_method_bind_call(mb, inst, args, arguments.size, null)
//        for (i in arguments.indices)
//            godot_variant_destroy(args[i])
//        return Variant(result)
//    }
//}
//
//        """.trimIndent())
//
//        for (icall in icalls)
//            append(icall.generate())
//    })

/*
    // Prints all virtual methods with non-Unit result type
    for (cl in classes)
        if (cl.isInstanciable)
            for (m in cl.methods)
                if (m.isVirtual && m.returnType != "Unit") {
                    var flag = true

                    for (p in cl.properties)
                        if (p.getter == m.name || p.setter == m.name) {
                            flag = false
                            return
                        }

                    if (flag)
                        println("${cl.name}: ${m.name} of type ${m.returnType}")
                }
*/
/*
    // Invalid getters ans setters
    for (cl in classes)
        for (m in cl.methods)
            props@ for (p in cl.properties) {
                if ((m.name == p.getter && (m.returnType == "Unit" || m.arguments.size > 1 || (m.arguments.size == 1 && m.arguments[0].type != "Int"))) ||
                        (m.name == p.setter && (m.returnType != "Unit" || m.arguments.size > 2 || (m.arguments.size == 2 && m.arguments[0].type != "Int")))) {
                    println("${cl.name}: ${m.name} of type ${m.returnType}")
                    break@props
                }
            }
*/
}