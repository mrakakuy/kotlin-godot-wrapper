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
            val argTypeStr = arg.type.convertTypeForICalls()
            var argType: TypeName = ClassName(argTypeStr.getPackage(), argTypeStr)
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
        codeBlockBuilder.add("%M {\n", MemberName("kotlinx.cinterop", "memScoped"))
        if (shouldReturn) {
            if (isPrimitive) {
                codeBlockBuilder.add("    val retVar = %M<%T>()\n",
                        MemberName("kotlinx.cinterop", "alloc"),
                        ClassName("kotlinx.cinterop", "${returnType}Var"))
            }
            else {
                codeBlockBuilder.add("    val retVar = %M<%T>(8)\n",
                        MemberName("kotlinx.cinterop", "allocArray"),
                        ClassName("kotlinx.cinterop", "ByteVar"))
            }
        }
        codeBlockBuilder.add("    val args = %M<%T>(${arguments.size + 1})\n",
                MemberName("kotlinx.cinterop", "allocArray"),
                ClassName("kotlinx.cinterop", "COpaquePointerVar"))
        codeBlockBuilder.add(buildString {arguments.withIndex().forEach {
            val i = it.index
            appendln("    args[$i] = arg$i${if (it.value.nullable) "?.getRawMemory(memScope)" else ".getRawMemory(memScope)"}\n")
        }})
        codeBlockBuilder.add("    args[${arguments.size}] = null\n")
        if (shouldReturn) {
            if (isPrimitive) {
                codeBlockBuilder.add("    %M(mb, inst, args, retVar.%M)\n",
                        MemberName("godot.gdnative", "godot_method_bind_ptrcall"),
                        MemberName("kotlinx.cinterop", "ptr")
                )
                codeBlockBuilder.add("    ret = retVar.%M\n", MemberName("kotlinx.cinterop", "value"))
            }
            else {
                codeBlockBuilder.add("    %M(mb, inst, args, retVar)\n",
                        MemberName("godot.gdnative", "godot_method_bind_ptrcall")
                )
                codeBlockBuilder.add("    ret = %T(retVar)\n", returnTypeClass)
            }
        }
        else
            codeBlockBuilder.add("    %M(mb, inst, args, null)\n",
                    MemberName("godot.gdnative", "godot_method_bind_ptrcall")
            )
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

    override fun equals(other: Any?): Boolean {
        if (other !is ICall)
            return false
        return this.name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}