import com.beust.klaxon.Json
import com.squareup.kotlinpoet.*
import java.io.File
import kotlin.math.sign


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
        @Json(name = "signals")
        val signals: List<Signal>,
        @Json(name = "methods")
        val methods: List<Method>,
        @Json(name = "enums")
        val enums: List<Enum>) {
    
    val oldName: String = name
    val shouldGenerate: Boolean

    init {
        name = name.escapeUnderscore()
        baseClass = baseClass.escapeUnderscore()

        shouldGenerate = name != "GlobalConstants"
    }


    fun generate(path: String, tree: Graph<Class>, icalls: MutableSet<ICall>) {
        for (p in properties) for (m in methods) p.applyGetterOrSetter(m)

        val out = File("$path/$name.kt")
        out.parentFile.mkdirs()
        out.createNewFile()

        if (shouldGenerate) {
            val packageName = "godot"
            val className = ClassName(packageName, name)
            val typeBuilder = TypeSpec.classBuilder(className).addModifiers(KModifier.OPEN)
            typeBuilder.superclass(ClassName(packageName, if (baseClass.isEmpty()) "GodotObject" else baseClass))
            if (isInstanciable) {
                typeBuilder.addFunction(
                        FunSpec.constructorBuilder()
                                .callSuperConstructor("\"${if (name != "Thread") name else "_Thread"}\"")
                                .build()
                )
            }
            else {
                typeBuilder.addFunction(
                        FunSpec.constructorBuilder()
                                .callSuperConstructor("\"\"")
                                .build()
                )
            }
            typeBuilder.addFunction(
                    FunSpec.constructorBuilder()
                            .addParameter("variant", ClassName("godot.core", "Variant"))
                            .callSuperConstructor("variant")
                            .build()
            )
            typeBuilder.addFunction(
                    FunSpec.constructorBuilder()
                            .addModifiers(KModifier.INTERNAL)
                            .addParameter("mem", ClassName("kotlinx.cinterop", "COpaquePointer"))
                            .callSuperConstructor("mem")
                            .build()
            )
            typeBuilder.addFunction(
                    FunSpec.constructorBuilder()
                            .addModifiers(KModifier.INTERNAL)
                            .addParameter("name", String::class)
                            .callSuperConstructor("name")
                            .build()
            )
            for (enum in enums) typeBuilder.addType(enum.generated)
            val signalClassBuilder = TypeSpec.classBuilder("Signal")
            val signalCompanionObjectBuilder = TypeSpec.companionObjectBuilder()
            for (signal in signals) signalCompanionObjectBuilder.addProperty(signal.generated)
            signalClassBuilder.addType(signalCompanionObjectBuilder.build())
            typeBuilder.addType(signalClassBuilder.build())
            val kotlinFile = FileSpec.builder(packageName, className.simpleName).addType(typeBuilder.build()).build()
            kotlinFile.writeTo(System.out)
        }

        out.writeText(buildString {
            appendln("@file:Suppress(\"unused\", \"ClassName\", \"EnumEntryName\", \"FunctionName\", \"SpellCheckingInspection\", \"PARAMETER_NAME_CHANGED_ON_OVERRIDE\", \"UnusedImport\", \"PackageDirectoryMismatch\")")
            appendln("package godot")
            appendln()
            if (shouldGenerate) {
                appendln("import godot.gdnative.*")
                appendln("import godot.core.*")
                appendln("import godot.utils.*")
                appendln("import godot.icalls.*")
                appendln("import kotlinx.cinterop.*")
                appendln()
            }
            appendln()
            appendln("// NOTE: THIS FILE IS AUTO GENERATED FROM JSON API CONFIG")
            appendln()
            appendln()


            var constantsPrefix = ""
            if (shouldGenerate) {
                append("open class $name : ").append(if (baseClass == "") "GodotObject" else baseClass).appendln(" {")

                append("    ")
                if (isInstanciable)
                    // LUL, ask Godot's author for any explanation about _Thread
                    appendln("constructor() : super(\"${if (name != "Thread") name else "_Thread"}\")")
                else
                    appendln("private constructor() : super(\"\")")
                appendln("    constructor(variant: Variant) : super(variant)")
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


                appendln("    // Signals")
                appendln("    class Signal {")
                appendln("        companion object {")
                for (sig in signals)
                    appendln("            ${sig.gene()}")
                appendln("        }")
                appendln("    }")
                appendln()
                appendln()


                if (isSingleton)
                    append("    @ThreadLocal") // TODO: remove later, fixed in konan master
                appendln("    companion object {")

                append(generateCasts(tree))

                constantsPrefix = "        "
            }


            appendln("$constantsPrefix// Constants")
            for (constant in constants)
                appendln("${constantsPrefix}const val ${constant.key}: Long = ${constant.value}")
            appendln()
            appendln()


            if (shouldGenerate) {
                if (isSingleton)
                    appendln("        private val rawMemory: COpaquePointer by lazy { getSingleton(\"$name\", \"$oldName\") }")
                else
                    appendln("    }")
                appendln()
                appendln()


                val singletonPrefix = if (isSingleton) "    " else ""


                appendln("$singletonPrefix    // Properties")
                for (prop in properties)
                    append(prop.generate(singletonPrefix, this@Class, tree, icalls))
                appendln()
                appendln()


                appendln("$singletonPrefix    // Methods")
                for (method in methods)
                    append(method.generate(singletonPrefix, this@Class, tree, icalls))


                if (isSingleton)
                    appendln("    }")
                appendln("}")
            }
        })
    }


    private fun generateCasts(tree: Graph<Class>): String {
        return buildString {
            var node = tree.nodes.find { it.value.name == name }!!.parent

            while (node != null) {
                appendln("        infix fun from(other: ${node.value.name}): $name = $name(\"\").apply { setRawMemory(other.rawMemory) }")
                node = node.parent
            }
            appendln("        infix fun from(other: Variant): $name = fromVariant($name(\"\"), other)")
            appendln()
            appendln()
        }
    }
}