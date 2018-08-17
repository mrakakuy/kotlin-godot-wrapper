import com.beust.klaxon.Json
import java.io.File


class Class(
        @Json(name = "name")
        var name: String,
        @Json(name = "base_class")
        var baseClass: String,
        @Json(name = "singleton")
        val isSingleton: Boolean,
        @Json(name = "instanciable")
        val isInstanciable: Boolean,
        @Json(name = "constants")
        val constants: Map<String, Int>,
        @Json(name = "properties")
        val properties: List<Property>,
        @Json(name = "methods")
        val methods: List<Method>,
        @Json(name = "enums")
        val enums: List<Enum>
) {
    val oldName: String = name

    init {
        name = name.escapeUnderscore()
        baseClass = baseClass.escapeUnderscore()
    }


    fun generate(path: String, tree: Graph<Class>, icalls: MutableSet<ICall>) {
        val out = File("$path/$name.kt")
        out.parentFile.mkdirs()
        out.createNewFile()

        out.writeText(buildString {
            appendln("@file:Suppress(\"unused\", \"ClassName\", \"EnumEntryName\", \"FunctionName\", \"SpellCheckingInspection\", \"PARAMETER_NAME_CHANGED_ON_OVERRIDE\", \"UnusedImport\", \"PackageDirectoryMismatch\")")
            appendln("package kotlin.godot")
            appendln()
            appendln("import godot.*")
            appendln("import kotlin.godot.core.*")
            appendln("import kotlin.godot.icalls.*")
            appendln("import kotlinx.cinterop.*")
            appendln()
            appendln()
            appendln("// NOTE: THIS FILE IS AUTO GENERATED FROM JSON API CONFIG")
            appendln()
            appendln()


            append("open class $name : ").append(if (baseClass == "") "GodotObject" else baseClass).appendln(" {")

            append("    ")
            if (isInstanciable)
                appendln("constructor() : super(\"$name\")")
            else
                appendln("private constructor() : super(\"\")")
            appendln("    internal constructor(mem: COpaquePointer) : super(mem)")
            appendln("    internal constructor(name: String) : super(name)")
            appendln()
            appendln()


            appendln("    // Enums ")
            appendln()
            for (enum in enums)
                enum.generate(this)
            appendln()
            appendln()


            if (isSingleton)
                append("    @ThreadLocal") // TODO: remove later, fixed in konan master
            appendln("    companion object {")
            appendln("        // Constants")
            for (constant in constants)
                appendln("        const val ${constant.key}: Int = ${constant.value}")
            appendln()
            appendln()


            if (isSingleton) {
                appendln("        private val rawMemory: COpaquePointer by lazy {")
                appendln("            godot_global_get_singleton(\"$name\".cstr)!!")
                appendln("        }")
            } else
                appendln("    }")
            appendln()
            appendln()


            val prefix = if (isSingleton) "    " else ""


            appendln("$prefix    // Methods")
            for (method in methods)
                append(method.generate(prefix, this@Class, tree, icalls))


            if (isSingleton)
                appendln("    }")
            appendln("}")
        })
    }
}