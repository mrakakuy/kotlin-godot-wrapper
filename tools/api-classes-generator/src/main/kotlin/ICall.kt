import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class ICall(
        var returnType: String,
        val arguments: List<Argument>
) {
    val name: String = buildString {
        append("_icall_${if (returnType.isEnum()) "Long" else returnType}")

        for (arg in arguments) {
            append('_')
            if (arg.nullable)
                append('n')
            append(arg.type.convertTypeForICalls())
        }
    }

    private val returnTypeClass = ClassName(
            when {
                returnType.isEnum() -> "kotlin"
                else -> returnType.getPackage()
            }, if (returnType.isEnum()) "Long" else returnType)

    val iCallSpec by lazy {
        val spec = FunSpec.builder(name).addModifiers(KModifier.INTERNAL)
        spec.addParameter(
                ParameterSpec(
                        "mb",
                        ClassName("kotlinx.cinterop", "CPointer")
                                .parameterizedBy(ClassName("godot.gdnative", "godot_method_bind"))
                )
        )
        spec.addParameter("inst", ClassName("kotlinx.cinterop", "COpaquePointer"))
        arguments.withIndex().forEach {
            val arg = it.value
            var argType: TypeName = ClassName(if (arg.type.isCoreType()) "godot.core" else "godot", arg.type.convertTypeForICalls())
            if (arg.nullable) argType = argType.copy(nullable = true)
            if (arg.nullable) argType.copy(nullable = true)
            spec.addParameter(
                    "arg${it.index}",
                    argType)
        }
        val shouldReturn = returnType != "Unit"
        val isPrimitive = returnType.isPrimitive()
        if (shouldReturn) {
            spec.returns(returnTypeClass)
            if (isPrimitive) spec.addStatement("var ret: %N = ${returnType.defaultValue()}", returnTypeClass.simpleName)
            else spec.addStatement("lateinit var ret: %N", returnTypeClass.simpleName)
        }

        val codeBlockBuilder = CodeBlock.builder()
        codeBlockBuilder.add("memscope {\n")
        if (shouldReturn) {
            if (isPrimitive) {
                codeBlockBuilder.add("    val retVar = %M<%N>()\n",
                        MemberName("kotlinx.cinterop", "alloc"),
                        MemberName("kotlinx.cinterop", "${returnType}Var"))
            }
            else {
                codeBlockBuilder.add("    val retVar = %M<%N>(8)\n",
                        MemberName("kotlinx.cinterop", "allocArray"),
                        MemberName("kotlinx.cinterop", "ByteVar"))
            }
        }
        codeBlockBuilder.add("    val args = %M<%N>(${arguments.size + 1})\n",
                MemberName("kotlinx.cinterop", "allocArray"),
                MemberName("kotlinx.cinterop", "COpaquePointerVar"))
        codeBlockBuilder.add(buildString {arguments.withIndex().forEach {
            val i = it.index
            appendln("    args[$i] = arg$i${if (it.value.nullable) "?.getRawMemory(memScope)" else ".getRawMemory(memScope)"}\n")
        }})
        codeBlockBuilder.add("    args[${arguments.size}] = null\n")
        codeBlockBuilder.add("    %M(mb, inst, args, retVar.ptr)\n", MemberName("godot.gdnative", "godot_method_bind_ptrcall"))
        if (shouldReturn) codeBlockBuilder.add("    ret = retVar.value\n")
        codeBlockBuilder.add("}\n")
        if (shouldReturn) codeBlockBuilder.add("return ret")
        spec.addCode(codeBlockBuilder.build())
        spec.build()
    }

    init {
        if (returnType.isEnum()) {
            returnType = "Long"
        }
    }


    fun generate(): String {
        return buildString {
            append("internal fun $name(mb: CPointer<godot_method_bind>, inst: COpaquePointer")

            for ((i, arg) in arguments.withIndex()) {
                append(", arg$i: ${arg.type.convertTypeForICalls()}")
                if (arg.nullable)
                    append('?')
            }
            append(')')



            val shouldReturn = returnType != "Unit"
            val isPrimitive = returnType.isPrimitive()


            if (shouldReturn)
                append(": $returnType")
            appendln(" {")


            if (shouldReturn) {
                append("    ")
                if (isPrimitive)
                    appendln("var ret: $returnType = ${returnType.defaultValue()}")
                else
                    appendln("lateinit var ret: $returnType")
            }


            appendln("    memScoped {")


            if (shouldReturn) {
                append("        val retVar = alloc")
                if (isPrimitive)
                    appendln("<${returnType}Var>()")
                else
                    appendln("Array<ByteVar>(8)")
            }


            appendln("        val args = allocArray<COpaquePointerVar>(${arguments.size + 1})")
            for ((i, arg) in arguments.withIndex()) {
                append("        args[$i] = arg$i")
                if (arg.nullable)
                    append('?')
                appendln(".getRawMemory(memScope)")
            }
            appendln("        args[${arguments.size}] = null")

            append("        godot_method_bind_ptrcall(mb, inst, args, ")
            if (shouldReturn)
                if (isPrimitive)
                    append("retVar.ptr")
                else
                    append("retVar")
            else
                append("null")
            appendln(')')


            if (shouldReturn)
                if (isPrimitive)
                    appendln("        ret = retVar.value")
                else
                    appendln("        ret = $returnType(retVar)")


            appendln("    }")


            if (shouldReturn)
                appendln("    return ret")
            appendln("}")
            appendln()
        }
    }



    override fun equals(other: Any?): Boolean {
        if (other !is ICall)
            return false
        return this.name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}