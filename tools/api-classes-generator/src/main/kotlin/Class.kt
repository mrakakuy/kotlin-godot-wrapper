import com.beust.klaxon.Json
import com.squareup.kotlinpoet.*
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
        @Json(name = "signals")
        val signals: List<Signal>,
        @Json(name = "methods")
        val methods: List<Method>,
        @Json(name = "enums")
        val enums: List<Enum>) {
    
    val oldName: String = name
    val shouldGenerate: Boolean

    lateinit var className: ClassName

    init {
        name = name.escapeUnderscore()
        baseClass = baseClass.escapeUnderscore()

        shouldGenerate = name != "GlobalConstants"
    }


    fun generate(path: String, tree: Graph<Class>, icalls: MutableSet<ICall>) {
        properties.forEach { propertie ->
            methods.forEach {
                propertie applyGetterOrSetter it
            }
        }

        val out = File("$path/$name.kt")
        out.parentFile.mkdirs()
        out.createNewFile()

        val packageName = "godot"
        className = ClassName(packageName, name)
        if (shouldGenerate) {

            //Create Types
            val typeBuilder = TypeSpec.classBuilder(className).addModifiers(KModifier.OPEN)

            //Set super class
            typeBuilder.superclass(ClassName(packageName, if (baseClass.isEmpty()) "GodotObject" else baseClass))

            //Constructors
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
            val cOpaquePointerClass = ClassName("kotlinx.cinterop", "COpaquePointer")
            typeBuilder.addFunction(
                    FunSpec.constructorBuilder()
                            .addModifiers(KModifier.INTERNAL)
                            .addParameter("mem", cOpaquePointerClass)
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

            //Enums
            enums.forEach {
                typeBuilder.addType(it.generated)
            }
            val signalClassBuilder = TypeSpec.classBuilder("Signal")
            val signalCompanionObjectBuilder = TypeSpec.companionObjectBuilder()

            //Signals
            signals.forEach {
                signalCompanionObjectBuilder.addProperty(it.generated)
            }
            signalClassBuilder.addType(signalCompanionObjectBuilder.build())

            val baseCompanion = TypeSpec.companionObjectBuilder()
            if (isSingleton) baseCompanion.addAnnotation(ClassName("kotlin.native", "ThreadLocal"))
            //Casts
            generateCasts(tree).forEach {
                baseCompanion.addFunction(it)
            }
            //Constants
            constants.forEach { (key, value) ->
                baseCompanion.addProperty(
                        PropertySpec.builder(key, Long::class)
                                .addModifiers(KModifier.CONST, KModifier.FINAL).initializer("%L", value).build()
                )
            }

            val rawMemorySpec = PropertySpec.builder(
                    "rawMemory",
                    cOpaquePointerClass,
                    KModifier.PRIVATE, KModifier.FINAL)
                    .delegate("lazy { getSingleton(\"$name\", \"$oldName\") }")
            if (isSingleton) baseCompanion.addProperty(rawMemorySpec.build())

            typeBuilder.addType(baseCompanion.build())

            //Build Type and create file
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


    private fun generateCasts(tree: Graph<Class>): List<FunSpec> {
        val funSpecs = mutableListOf<FunSpec>()
        var node = tree.nodes.find { it.value.name == name }!!.parent

        while (node != null) {
            funSpecs.add(
                    FunSpec.builder("from")
                    .addModifiers(KModifier.INFIX)
                    .addParameter("other", ClassName(if (classes.contains(node.value)) "godot" else "godot.core", node.value.name))
                    .addStatement("return $name(\"\").apply { setRawMemory(other.rawMemory) }").build()
            )
            node = node.parent
        }
        funSpecs.add(
                FunSpec.builder("from")
                        .addModifiers(KModifier.INFIX)
                        .addParameter("other", ClassName("godot.core", "Variant"))
                        .addStatement("return fromVariant($name(\"\"), other)")
                        .build()
        )
        return funSpecs
    }
}