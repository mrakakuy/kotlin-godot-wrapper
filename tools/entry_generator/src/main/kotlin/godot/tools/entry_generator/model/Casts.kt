package godot.tools.entry_generator.model


fun String.castToVariant(value: String): String {
    return "Variant($value)"
}


fun String.isPrimitive(): Boolean {
    val primitives = listOf("Byte", "Int", "Short", "Long", "Double", "Float", "Boolean")
    return primitives.find { s -> s == this } != null
}

fun String.isCoreType(): Boolean {
    val coreTypes = listOf(
            "GDArray",
            "Basis",
            "Color",
            "Dictionary",
            "GodotError",
            "NodePath",
            "Plane",
            "PoolByteArray",
            "PoolIntArray",
            "PoolRealArray",
            "PoolStringArray",
            "PoolVector2Array",
            "PoolVector3Array",
            "PoolColorArray",
            "PoolIntArray",
            "PoolRealArray",
            "Quat",
            "Rect2",
            "AABB",
            "RID",
            "String",
            "Transform",
            "Transform2D",
            "Variant",
            "Vector2",
            "Vector3")
    return coreTypes.find { s -> s == this } != null
}


fun String.castFromRawMemory(value: String): String {
    return this.castFromVariant("Variant($value)")
}


fun String.castFromVariant(value: String): String {
    if (this == "Variant")
        return value
    if (this.isCoreType() || this.isPrimitive())
        return "$value.to$this()"
    return "$this($value)"
}


fun String.getVariantType(): String {
    return when (this) {
        "Byte","Short","Int","Long" -> "Variant.Type.INT"
        "Float","Double" -> "Variant.Type.REAL"
        "Boolean" -> "Variant.Type.BOOL"
        "String" -> "Variant.Type.STRING"
        "RID" -> "Variant.Type._RID"
        "GDArray" -> "Variant.Type.ARRAY"
        "Vector2","Rect2","Vector3","Transform2D","Plane","Quat","Rect3","Basis","Transform","Color","Dictionary" -> "Variant.Type.${this.toUpperCase()}"
        "NodePath" -> "Variant.Type.NODE_PATH"
        "PoolByteArray" -> "Variant.Type.POOL_BYTE_ARRAY"
        "PoolIntArray" -> "Variant.Type.POOL_INT_ARRAY"
        "PoolReadArray" -> "Variant.Type.POOL_REAL_ARRAY"
        "PoolStringArray" -> "Variant.Type.POOL_STRING_ARRAY"
        "PoolVector2Array" -> "Variant.Type.POOL_VECTOR2_ARRAY"
        "PoolVector3Array" -> "Variant.Type.POOL_VECTOR3_ARRAY"
        "PoolColorArray" -> "Variant.Type.POOL_COLOR_ARRAY"
        else -> "Variant.Type.OBJECT"
    }
}

fun String.getRPCType(): String {
    return when (this) {
        "Remote" -> "godot_method_rpc_mode.GODOT_METHOD_RPC_MODE_REMOTE"
        "Sync" -> "godot_method_rpc_mode.GODOT_METHOD_RPC_MODE_SYNC"
        "Master" -> "godot_method_rpc_mode.GODOT_METHOD_RPC_MODE_MASTER"
        "Slave" -> "godot_method_rpc_mode.GODOT_METHOD_RPC_MODE_SLAVE"
        "RemoteSync" -> "godot_method_rpc_mode.GODOT_METHOD_RPC_MODE_REMOTESYNC"
        "MasterSync" -> "godot_method_rpc_mode.GODOT_METHOD_RPC_MODE_MASTERSYNC"
        "SlaveSync" -> "godot_method_rpc_mode.GODOT_METHOD_RPC_MODE_SLAVESYNC"
        else -> "godot_method_rpc_mode.GODOT_METHOD_RPC_MODE_DISABLED"
    }
}