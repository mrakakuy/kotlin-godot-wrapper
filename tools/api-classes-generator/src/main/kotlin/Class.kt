import com.beust.klaxon.Json
import com.squareup.kotlinpoet.*
import java.io.File
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy


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
    val additionalImports = mutableListOf<Pair<String, String>>()

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

        val out = File(path)
        out.parentFile.mkdirs()

        val packageName = "godot"
        className = ClassName(packageName, name)
        if (shouldGenerate) {

            //Create Types
            val typeBuilder = TypeSpec.classBuilder(className).addModifiers(KModifier.OPEN)

            //Set super class
            typeBuilder.superclass(ClassName(packageName, if (baseClass.isEmpty()) "GodotObject" else baseClass))

            //Constructors
            typeBuilder.addFunction(
                    FunSpec.constructorBuilder()
                            .callSuperConstructor(if (isInstanciable) "\"${if (name != "Thread") name else "_Thread"}\"" else "\"\"")
                            .build()
            )
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

            //RawMemory lazy property
            val rawMemorySpec = PropertySpec.builder(
                    "rawMemory",
                    cOpaquePointerClass,
                    KModifier.PRIVATE, KModifier.FINAL)
                    .delegate("lazy{ %M(\"$name\", \"$oldName\") }", MemberName("godot.utils", "getSingleton"))
            if (isSingleton) baseCompanion.addProperty(rawMemorySpec.build())

            //Properties
            val receiverType = if (isSingleton) baseCompanion else typeBuilder
            properties.forEach {
                val propertySpec = it.generate(this, tree, icalls)
                if (propertySpec != null) {
                    receiverType.addProperty(propertySpec)
                    val parameterType = it.type
                    val parameterTypeName = ClassName(if (parameterType.isCoreType()) "godot.core" else "godot", parameterType)
                    if (it.hasValidSetter && parameterType.isCoreTypeAdaptedForKotlin()) {
                        val parameterName = it.name
                        val propertyFunSpec = FunSpec.builder(parameterName)
                        if (!isSingleton) {
                            if (tree.doAncestorsHaveProperty(this, it)) {
                                propertyFunSpec.addModifiers(KModifier.OVERRIDE)
                            } else {
                                propertyFunSpec.addModifiers(KModifier.OPEN)
                            }
                        }
                        propertyFunSpec.addParameter(
                                ParameterSpec.builder(
                                        "schedule",
                                        LambdaTypeName.get(
                                                parameters = *arrayOf(parameterTypeName),
                                                returnType = ClassName("kotlin", "Unit")
                                        )
                                ).build()
                        )
                        propertyFunSpec.returns(parameterTypeName)
                        propertyFunSpec.addStatement("""return $parameterName.apply {
                                            |    schedule(this)
                                            |    $parameterName = this
                                            |}
                                            |""".trimMargin())
                        receiverType.addFunction(propertyFunSpec.build())
                    }
                }
            }

            //Methods
            methods.forEach {
                if (!it.isVirtual) receiverType.addProperty(
                        PropertySpec.builder(
                        "${it.name}MethodBind",
                        ClassName("kotlinx.cinterop", "CPointer")
                                .parameterizedBy(ClassName("godot.gdnative", "godot_method_bind"))
                        ).delegate("%L%M(\"${oldName}\",\"${it.oldName}\")%L",
                                "lazy{ ",
                                MemberName("godot.utils", "getMB"),
                                " }"
                        )
                                .addModifiers(KModifier.PRIVATE, KModifier.FINAL).build()
                )
                receiverType.addFunction(it.generate(this, tree, icalls))
            }

            typeBuilder.addType(baseCompanion.build())

            //Build Type and create file
            typeBuilder.addType(signalClassBuilder.build())
            val fileBuilder = FileSpec.builder(packageName, className.simpleName)
                    .addType(typeBuilder.build())
            additionalImports.forEach {
                fileBuilder.addImport(it.first, it.second)
            }
            val kotlinFile = fileBuilder.build()
            kotlinFile.writeTo(out)
        }
    }


    private fun generateCasts(tree: Graph<Class>): List<FunSpec> {
        val funSpecs = mutableListOf<FunSpec>()
        var node = tree.nodes.find { it.value.name == name }!!.parent

        while (node != null) {
            funSpecs.add(
                    FunSpec.builder("from")
                    .addModifiers(KModifier.INFIX)
                    .addParameter("other", ClassName(if (node.value.name.isCoreType()) "godot.core" else "godot", node.value.name))
                    .addStatement("return $name(\"\").apply { setRawMemory(other.rawMemory) }").build()
            )
            node = node.parent
        }
        funSpecs.add(
                FunSpec.builder("from")
                        .addModifiers(KModifier.INFIX)
                        .addParameter("other", ClassName("godot.core", "Variant"))
                        .addStatement("return %M($name(\"\"), other)", MemberName("godot.utils", "fromVariant"))
                        .build()
        )
        return funSpecs
    }
}