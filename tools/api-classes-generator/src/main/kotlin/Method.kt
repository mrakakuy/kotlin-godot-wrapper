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
        if (!isVirtual) {
            val constructedICall = constructICall(callArguments, icalls)
            generatedFunBuilder.addStatement("%L%L%M%L%L",
                    if (shouldReturn) "return " else "",
                    if (returnType.isEnum()) "${returnType.removeEnumPrefix()}.fromInt("
                    else if (hasVarargs && returnType != "Variant") "$returnType from "
                    else "",
                    MemberName("godot.icalls", constructedICall.first),
                    constructedICall.second,
                    if (returnType.isEnum()) ")" else ""
            )
        }
        else {
            if (shouldReturn) generatedFunBuilder.addStatement("throw %T(\"$oldName is not implemented for ${cl.name}\")", NotImplementedError::class)
        }
        return generatedFunBuilder.build()
    }

    fun gene(prefix: String, cl: Class, tree: Graph<Class>, icalls: MutableSet<ICall>): String {
        return buildString {
            if (!isVirtual) {
                appendln("$prefix    private val ${name}MethodBind: CPointer<godot_method_bind> by lazy { getMB(\"${cl.oldName}\", \"$oldName\") }")
            }

            // Uncomment to disable method implementation generation
            //if (isGetterOrSetter)
            //    return@buildString

            append("$prefix    ")
            if (!cl.isSingleton)
                if (tree.doAncestorsHaveMethod(cl, this@Method))
                    append("override ")
                else
                    append("open ")
            append("fun $name(")

            val methodArguments = StringBuilder()
            for ((i, arg) in arguments.withIndex()) {
                if (i != 0 || !hasVarargs)
                    methodArguments.append(", ")
                methodArguments.append(arg.name)

                if (arg.type.isEnum())
                    methodArguments.append(".id")

                append("${arg.name}: ${arg.type.removeEnumPrefix()}${arg.applyDefaultValue()}")

                if (i != arguments.size - 1 || hasVarargs)
                    append(", ")
            }
            if (hasVarargs) {
                if (!methodArguments.isEmpty())
                    methodArguments.append(", ")
                append("vararg __var_args: Any?")
            }


            val shouldReturn = returnType != "Unit"


            append(')')
            if (shouldReturn)
                append(": ${returnType.removeEnumPrefix()}")
            appendln(" {")


            if (!isVirtual) {
                append("$prefix        ")

                var suffix = ""

                if (shouldReturn) {
                    append("return ")

                    if (returnType.isEnum()) {
                        append("${returnType.removeEnumPrefix()}.fromInt(")
                        suffix = ")"
                    }
                    else if (hasVarargs && returnType != "Variant")
                        append("$returnType from ")
                }

                append(constructICall(methodArguments.toString(), icalls))
                appendln(suffix)
            } else {// TODO: add varargs and virtual methods
                if (shouldReturn) {
                    appendln("$prefix        throw NotImplementedError(\"$oldName is not implemented for ${cl.name}\")")
                }
            }


            appendln("$prefix    }")
            appendln()
            appendln()
        }
    }


    private fun constructICall(methodArguments: String, icalls: MutableSet<ICall>): Pair<String, String> {
        if (hasVarargs)
            return "_icall_varargs" to "(${name}MethodBind, this.rawMemory, arrayOf($methodArguments*__var_args))"

        val icall = ICall(returnType, arguments)
        icalls.add(icall)
        return icall.name to "(${name}MethodBind, this.rawMemory$methodArguments)"
    }
}