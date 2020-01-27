import com.beust.klaxon.Json
import com.squareup.kotlinpoet.*


open class Method(
        @Json(name = "name")
        var name: String,
        @Json(name = "return_type")
        var returnType: String,
        @Json(name = "is_virtual")
        val isVirtual: Boolean,
        @Json(name = "has_varargs")
        val hasVarargs: Boolean,
        @Json(name = "arguments")
        val arguments: List<Argument>) {

    val oldName: String = name

    init {
        if (!isVirtual)
            name = name.convertToCamelCase()
        returnType = returnType.convertTypeToKotlin()
    }

    var isGetterOrSetter: Boolean = false

    fun generate(cl: Class, tree: Graph<Class>, icalls: MutableSet<ICall>): FunSpec {
        // Uncomment to disable method implementation generation
        //if (isGetterOrSetter) return null
        val generatedFunBuilder = FunSpec.builder(name)
        val modifiers = mutableListOf<KModifier>()
        if (!cl.isSingleton) modifiers.add(if (tree.doAncestorsHaveMethod(cl, this)) KModifier.OVERRIDE else KModifier.OPEN)
        generatedFunBuilder.addModifiers(modifiers)

        //Not sure this is the best way to generate arguments for iCall
        val callArguments = buildString {
            arguments.withIndex().forEach {
                val index = it.index
                val argument = it.value

                if (index != 0 || !hasVarargs) append(", ")
                append(argument.name)
                if (argument.type.isEnum()) append(".id")

                val parameterBuilder = ParameterSpec.builder(
                        argument.name,
                        ClassName(argument.type.getPackage(),
                                argument.type.removeEnumPrefix()).copy(nullable = argument.nullable)
                )
                if (argument.applyDefault != null) parameterBuilder.defaultValue(argument.applyDefault)
                generatedFunBuilder.addParameter(parameterBuilder.build())
            }
            if (hasVarargs && !isEmpty()) append(", ")
        }
        if (hasVarargs) generatedFunBuilder.addParameter("__var_args", Any::class.asTypeName().copy(nullable = true), KModifier.VARARG)
        val shouldReturn = returnType != "Unit"
        if (shouldReturn) generatedFunBuilder.returns(ClassName(returnType.getPackage(), returnType.removeEnumPrefix()))
        if (returnType.isEnum()) {
            val type = returnType.removeEnumPrefix()
            if (type.contains('.')) {
                cl.additionalImports.add(returnType.getPackage() to type.split('.')[0])
            }
        }
        if (!isVirtual) {
            val constructedICall = constructICall(callArguments, icalls)
            generatedFunBuilder.addStatement("%L%L%M%L%L",
                    if (shouldReturn) "return " else "",
                    if (returnType.isEnum()) "${returnType.removeEnumPrefix()}.fromInt( "
                    else if (hasVarargs && returnType != "Variant") "$returnType from "
                    else "",
                    MemberName("godot.icalls", constructedICall.first),
                    constructedICall.second,
                    if (returnType.isEnum()) ")" else ""
            )
        }
        else {
            if (shouldReturn) generatedFunBuilder.addStatement("%L %T(%S)", "throw", NotImplementedError::class, "$oldName is not implemented for ${cl.name}")
        }
        return generatedFunBuilder.build()
    }


    private fun constructICall(methodArguments: String, icalls: MutableSet<ICall>): Pair<String, String> {
        if (hasVarargs)
            return "_icall_varargs" to "( ${name}MethodBind, this.rawMemory, arrayOf($methodArguments*__var_args))"

        val icall = ICall(returnType, arguments)
        icalls.add(icall)
        return icall.name to "( ${name}MethodBind, this.rawMemory$methodArguments)"
    }
}