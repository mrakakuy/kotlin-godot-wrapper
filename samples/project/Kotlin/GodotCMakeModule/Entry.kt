@file:Suppress("PackageDirectoryMismatch", "FunctionName", "UNUSED_ANONYMOUS_PARAMETER")
package kotlin.godot.entry

import konan.internal.CName
import kotlinx.cinterop.*
import godot.gdnative.*
import godot.core.*
import godot.*
import godot.registration.*


// NOTE THIS FILE IS AUTO-GENERATED


@CName("godot_gdnative_init")
fun GDNativeInit(options: godot_gdnative_init_options) {
    godot_wrapper_gdnative_init(options.ptr)
}


@CName("godot_gdnative_terminate")
fun GDNativeTerminate(options: godot_gdnative_terminate_options) {
    godot_wrapper_gdnative_terminate(options.ptr)
}


@CName("godot_nativescript_init")
fun NativeScriptInit(handle: COpaquePointer) {
    godot_wrapper_nativescript_init(handle)

    // Let's do some registration magic.
    registerClass("godot.samples.games.shmup.Player", "KinematicBody2D", udcBridge0(), udcBridge1())
    registerMethod("godot.samples.games.shmup.Player", "_ready", udcBridge2())
    registerMethod("godot.samples.games.shmup.Player", "_process", udcBridge3())
    registerClass("godot.samples.games.shmup.Bullet", "Area2D", udcBridge4(), udcBridge5())
    registerMethod("godot.samples.games.shmup.Bullet", "_process", udcBridge6())
    registerClass("godot.samples.games.shmup.Enemy", "Area2D", udcBridge7(), udcBridge8())
    registerMethod("godot.samples.games.shmup.Enemy", "_ready", udcBridge9())
    registerMethod("godot.samples.games.shmup.Enemy", "_process", udcBridge10())
    registerProperty("godot.samples.games.shmup.Enemy", "health", true, udcBridge11(), udcBridge12(), udcBridge13())
    registerClass("godot.samples.games.shmup.EnemyPath", "Path2D", udcBridge14(), udcBridge15())
    registerMethod("godot.samples.games.shmup.EnemyPath", "_ready", udcBridge16())
    registerClass("godot.samples.games.shmup.Stage", "Node", udcBridge17(), udcBridge18())
    registerMethod("godot.samples.games.shmup.Stage", "_ready", udcBridge19())
    registerMethod("godot.samples.games.shmup.Stage", "_process", udcBridge20())
    registerClass("godot.samples.games.shmup.Camera", "Camera2D", udcBridge21(), udcBridge22())
    registerMethod("godot.samples.games.shmup.Camera", "_process", udcBridge23())
    registerMethod("godot.samples.games.shmup.Camera", "shake", udcBridge24())
    registerClass("godot.samples.games.dodge.Player", "Area2D", udcBridge25(), udcBridge26())
    registerMethod("godot.samples.games.dodge.Player", "_ready", udcBridge27())
    registerMethod("godot.samples.games.dodge.Player", "_process", udcBridge28())
    registerMethod("godot.samples.games.dodge.Player", "_on_Player_body_entered", udcBridge29())
    registerMethod("godot.samples.games.dodge.Player", "start", udcBridge30())
    registerProperty("godot.samples.games.dodge.Player", "speed", true, udcBridge31(), udcBridge32(), udcBridge33())
    registerSignal("godot.samples.games.dodge.Player", "hit")
    registerClass("godot.samples.games.dodge.Main", "Node", udcBridge34(), udcBridge35())
    registerMethod("godot.samples.games.dodge.Main", "_ready", udcBridge36())
    registerMethod("godot.samples.games.dodge.Main", "gameOver", udcBridge37())
    registerMethod("godot.samples.games.dodge.Main", "newGame", udcBridge38())
    registerMethod("godot.samples.games.dodge.Main", "_onStartTimerTimeout", udcBridge39())
    registerMethod("godot.samples.games.dodge.Main", "_onScoreTimerTimeout", udcBridge40())
    registerMethod("godot.samples.games.dodge.Main", "_onMobTimerTimeout", udcBridge41())
    registerClass("godot.samples.games.dodge.HUD", "CanvasLayer", udcBridge42(), udcBridge43())
    registerMethod("godot.samples.games.dodge.HUD", "_ready", udcBridge44())
    registerMethod("godot.samples.games.dodge.HUD", "showMessage", udcBridge45())
    registerMethod("godot.samples.games.dodge.HUD", "showMenu", udcBridge46())
    registerMethod("godot.samples.games.dodge.HUD", "showGameOver", udcBridge47())
    registerMethod("godot.samples.games.dodge.HUD", "updateScore", udcBridge48())
    registerMethod("godot.samples.games.dodge.HUD", "_onMessageTimerTimeout", udcBridge49())
    registerMethod("godot.samples.games.dodge.HUD", "_onGameOverTimerTimeout", udcBridge50())
    registerMethod("godot.samples.games.dodge.HUD", "_onStartButtonPressed", udcBridge51())
    registerSignal("godot.samples.games.dodge.HUD", "startGame")
    registerClass("godot.samples.games.dodge.Mob", "RigidBody2D", udcBridge52(), udcBridge53())
    registerMethod("godot.samples.games.dodge.Mob", "_ready", udcBridge54())
    registerMethod("godot.samples.games.dodge.Mob", "_on_VisibilityNotifier2D_screen_exited", udcBridge55())
    registerProperty("godot.samples.games.dodge.Mob", "minSpeed", true, udcBridge56(), udcBridge57(), udcBridge58())
    registerProperty("godot.samples.games.dodge.Mob", "maxSpeed", true, udcBridge59(), udcBridge60(), udcBridge61())
}



// Bindings
private fun udcBinding0Constructor(): godot.samples.games.shmup.Player = godot.samples.games.shmup.Player()
private val udcBinding0 = ::udcBinding0Constructor
private fun udcBinding1Constructor(): godot.samples.games.shmup.Bullet = godot.samples.games.shmup.Bullet()
private val udcBinding1 = ::udcBinding1Constructor
private fun udcBinding2Constructor(): godot.samples.games.shmup.Enemy = godot.samples.games.shmup.Enemy()
private val udcBinding2 = ::udcBinding2Constructor
private fun udcBinding3Constructor(): godot.samples.games.shmup.EnemyPath = godot.samples.games.shmup.EnemyPath()
private val udcBinding3 = ::udcBinding3Constructor
private fun udcBinding4Constructor(): godot.samples.games.shmup.Stage = godot.samples.games.shmup.Stage()
private val udcBinding4 = ::udcBinding4Constructor
private fun udcBinding5Constructor(): godot.samples.games.shmup.Camera = godot.samples.games.shmup.Camera()
private val udcBinding5 = ::udcBinding5Constructor
private fun udcBinding6Constructor(): godot.samples.games.dodge.Player = godot.samples.games.dodge.Player()
private val udcBinding6 = ::udcBinding6Constructor
private fun udcBinding7Constructor(): godot.samples.games.dodge.Main = godot.samples.games.dodge.Main()
private val udcBinding7 = ::udcBinding7Constructor
private fun udcBinding8Constructor(): godot.samples.games.dodge.HUD = godot.samples.games.dodge.HUD()
private val udcBinding8 = ::udcBinding8Constructor
private fun udcBinding9Constructor(): godot.samples.games.dodge.Mob = godot.samples.games.dodge.Mob()
private val udcBinding9 = ::udcBinding9Constructor



// Bridges
private fun udcBridge0(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding0) }
}
private fun udcBridge1(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.shmup.Player>(mem) }
}
private fun udcBridge2(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.Player>("_ready", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._ready()
            }
            else -> noMethodToInvoke("_ready", "godot.samples.games.shmup.Player", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge3(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.Player>("_process", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Variant(args[0]!!).toFloat()
                obj._process(arg0)
            }
            else -> noMethodToInvoke("_process", "godot.samples.games.shmup.Player", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge4(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding1) }
}
private fun udcBridge5(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.shmup.Bullet>(mem) }
}
private fun udcBridge6(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.Bullet>("_process", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Variant(args[0]!!).toFloat()
                obj._process(arg0)
            }
            else -> noMethodToInvoke("_process", "godot.samples.games.shmup.Bullet", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge7(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding2) }
}
private fun udcBridge8(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.shmup.Enemy>(mem) }
}
private fun udcBridge9(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.Enemy>("_ready", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._ready()
            }
            else -> noMethodToInvoke("_ready", "godot.samples.games.shmup.Enemy", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge10(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.Enemy>("_process", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Variant(args[0]!!).toFloat()
                obj._process(arg0)
            }
            else -> noMethodToInvoke("_process", "godot.samples.games.shmup.Enemy", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge11(): CPointer<CFunction<(COpaquePointer?,COpaquePointer?) -> Unit>> {
    return staticCFunction { o, r -> get<godot.samples.games.shmup.Enemy>("health", "godot.samples.games.shmup.Enemy", o, r) {
        obj -> Variant(obj.health)
    }}
}
private fun udcBridge12(): CPointer<CFunction<(COpaquePointer?,COpaquePointer?) -> Unit>> {
    return staticCFunction { o, v -> set<godot.samples.games.shmup.Enemy>("health", "godot.samples.games.shmup.Enemy", o, v) {
        obj, value -> obj.health = value.toInt()
    }}
}
private fun udcBridge13(): Variant {
    return Variant(2)
}
private fun udcBridge14(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding3) }
}
private fun udcBridge15(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.shmup.EnemyPath>(mem) }
}
private fun udcBridge16(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.EnemyPath>("_ready", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._ready()
            }
            else -> noMethodToInvoke("_ready", "godot.samples.games.shmup.EnemyPath", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge17(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding4) }
}
private fun udcBridge18(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.shmup.Stage>(mem) }
}
private fun udcBridge19(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.Stage>("_ready", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._ready()
            }
            else -> noMethodToInvoke("_ready", "godot.samples.games.shmup.Stage", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge20(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.Stage>("_process", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Variant(args[0]!!).toFloat()
                obj._process(arg0)
            }
            else -> noMethodToInvoke("_process", "godot.samples.games.shmup.Stage", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge21(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding5) }
}
private fun udcBridge22(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.shmup.Camera>(mem) }
}
private fun udcBridge23(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.Camera>("_process", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Variant(args[0]!!).toFloat()
                obj._process(arg0)
            }
            else -> noMethodToInvoke("_process", "godot.samples.games.shmup.Camera", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge24(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.shmup.Camera>("shake", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            3 -> {
                args!!
                val arg0 = Variant(args[0]!!).toDouble()
                val arg1 = Variant(args[1]!!).toDouble()
                val arg2 = Variant(args[2]!!).toDouble()
                obj.shake(arg0, arg1, arg2)
            }
            else -> noMethodToInvoke("shake", "godot.samples.games.shmup.Camera", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge25(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding6) }
}
private fun udcBridge26(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.dodge.Player>(mem) }
}
private fun udcBridge27(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Player>("_ready", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._ready()
            }
            else -> noMethodToInvoke("_ready", "godot.samples.games.dodge.Player", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge28(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Player>("_process", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Variant(args[0]!!).toFloat()
                obj._process(arg0)
            }
            else -> noMethodToInvoke("_process", "godot.samples.games.dodge.Player", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge29(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Player>("_on_Player_body_entered", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Object(Variant(args[0]!!))
                obj._on_Player_body_entered(arg0)
            }
            else -> noMethodToInvoke("_on_Player_body_entered", "godot.samples.games.dodge.Player", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge30(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Player>("start", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Variant(args[0]!!).toVector2()
                obj.start(arg0)
            }
            else -> noMethodToInvoke("start", "godot.samples.games.dodge.Player", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge31(): CPointer<CFunction<(COpaquePointer?,COpaquePointer?) -> Unit>> {
    return staticCFunction { o, r -> get<godot.samples.games.dodge.Player>("speed", "godot.samples.games.dodge.Player", o, r) {
        obj -> Variant(obj.speed)
    }}
}
private fun udcBridge32(): CPointer<CFunction<(COpaquePointer?,COpaquePointer?) -> Unit>> {
    return staticCFunction { o, v -> set<godot.samples.games.dodge.Player>("speed", "godot.samples.games.dodge.Player", o, v) {
        obj, value -> obj.speed = value.toInt()
    }}
}
private fun udcBridge33(): Variant {
    return Variant(400)
}
private fun udcBridge34(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding7) }
}
private fun udcBridge35(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.dodge.Main>(mem) }
}
private fun udcBridge36(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Main>("_ready", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._ready()
            }
            else -> noMethodToInvoke("_ready", "godot.samples.games.dodge.Main", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge37(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Main>("gameOver", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj.gameOver()
            }
            else -> noMethodToInvoke("gameOver", "godot.samples.games.dodge.Main", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge38(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Main>("newGame", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj.newGame()
            }
            else -> noMethodToInvoke("newGame", "godot.samples.games.dodge.Main", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge39(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Main>("_onStartTimerTimeout", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._onStartTimerTimeout()
            }
            else -> noMethodToInvoke("_onStartTimerTimeout", "godot.samples.games.dodge.Main", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge40(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Main>("_onScoreTimerTimeout", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._onScoreTimerTimeout()
            }
            else -> noMethodToInvoke("_onScoreTimerTimeout", "godot.samples.games.dodge.Main", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge41(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Main>("_onMobTimerTimeout", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._onMobTimerTimeout()
            }
            else -> noMethodToInvoke("_onMobTimerTimeout", "godot.samples.games.dodge.Main", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge42(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding8) }
}
private fun udcBridge43(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.dodge.HUD>(mem) }
}
private fun udcBridge44(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.HUD>("_ready", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._ready()
            }
            else -> noMethodToInvoke("_ready", "godot.samples.games.dodge.HUD", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge45(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.HUD>("showMessage", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Variant(args[0]!!).toString()
                obj.showMessage(arg0)
            }
            else -> noMethodToInvoke("showMessage", "godot.samples.games.dodge.HUD", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge46(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.HUD>("showMenu", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj.showMenu()
            }
            else -> noMethodToInvoke("showMenu", "godot.samples.games.dodge.HUD", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge47(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.HUD>("showGameOver", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj.showGameOver()
            }
            else -> noMethodToInvoke("showGameOver", "godot.samples.games.dodge.HUD", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge48(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.HUD>("updateScore", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            1 -> {
                args!!
                val arg0 = Variant(args[0]!!).toInt()
                obj.updateScore(arg0)
            }
            else -> noMethodToInvoke("updateScore", "godot.samples.games.dodge.HUD", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge49(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.HUD>("_onMessageTimerTimeout", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._onMessageTimerTimeout()
            }
            else -> noMethodToInvoke("_onMessageTimerTimeout", "godot.samples.games.dodge.HUD", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge50(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.HUD>("_onGameOverTimerTimeout", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._onGameOverTimerTimeout()
            }
            else -> noMethodToInvoke("_onGameOverTimerTimeout", "godot.samples.games.dodge.HUD", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge51(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.HUD>("_onStartButtonPressed", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._onStartButtonPressed()
            }
            else -> noMethodToInvoke("_onStartButtonPressed", "godot.samples.games.dodge.HUD", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge52(): CPointer<CFunction<(COpaquePointer?) -> COpaquePointer?>> {
    return staticCFunction { mem -> constructFromRawMem(mem, udcBinding9) }
}
private fun udcBridge53(): CPointer<CFunction<(COpaquePointer?) -> Unit>> {
    return staticCFunction { mem -> deconstructFromRawMem<godot.samples.games.dodge.Mob>(mem) }
}
private fun udcBridge54(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Mob>("_ready", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._ready()
            }
            else -> noMethodToInvoke("_ready", "godot.samples.games.dodge.Mob", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge55(): CPointer<CFunction<(COpaquePointer?, COpaquePointer?, Int, COpaquePointer?) -> Unit>> {
    return staticCFunction { r, o, n, a -> invoke<godot.samples.games.dodge.Mob>("_on_VisibilityNotifier2D_screen_exited", r, o, n, a) { obj, numArgs, args -> run {
        when (numArgs) {
            0 -> {
                obj._on_VisibilityNotifier2D_screen_exited()
            }
            else -> noMethodToInvoke("_on_VisibilityNotifier2D_screen_exited", "godot.samples.games.dodge.Mob", numArgs)
        }
        return@run null }}
    }
}
private fun udcBridge56(): CPointer<CFunction<(COpaquePointer?,COpaquePointer?) -> Unit>> {
    return staticCFunction { o, r -> get<godot.samples.games.dodge.Mob>("minSpeed", "godot.samples.games.dodge.Mob", o, r) {
        obj -> Variant(obj.minSpeed)
    }}
}
private fun udcBridge57(): CPointer<CFunction<(COpaquePointer?,COpaquePointer?) -> Unit>> {
    return staticCFunction { o, v -> set<godot.samples.games.dodge.Mob>("minSpeed", "godot.samples.games.dodge.Mob", o, v) {
        obj, value -> obj.minSpeed = value.toInt()
    }}
}
private fun udcBridge58(): Variant {
    return Variant(150)
}
private fun udcBridge59(): CPointer<CFunction<(COpaquePointer?,COpaquePointer?) -> Unit>> {
    return staticCFunction { o, r -> get<godot.samples.games.dodge.Mob>("maxSpeed", "godot.samples.games.dodge.Mob", o, r) {
        obj -> Variant(obj.maxSpeed)
    }}
}
private fun udcBridge60(): CPointer<CFunction<(COpaquePointer?,COpaquePointer?) -> Unit>> {
    return staticCFunction { o, v -> set<godot.samples.games.dodge.Mob>("maxSpeed", "godot.samples.games.dodge.Mob", o, v) {
        obj, value -> obj.maxSpeed = value.toInt()
    }}
}
private fun udcBridge61(): Variant {
    return Variant(250)
}
