package godot.samples.games.multiplayer

import godot.*
import godot.core.*

class Player: KinematicBody2D() {

    var speed = 600.0
    var velocity = Vector2()

    override fun _process(delta: Double) {
        if (isNetworkMaster()) {
            velocity = Vector2()
            if (Input.isActionPressed("ui_right"))
                velocity.x += 1
            if (Input.isActionPressed("ui_left"))
                velocity.x -= 1
            if (Input.isActionPressed("ui_down"))
                velocity.y += 1
            if (Input.isActionPressed("ui_up"))
                velocity.y -= 1

            rpcUnreliable("setPosMov", position, velocity)
        }
        moveAndCollide(velocity * speed * delta)
    }

    fun setPosMov(pos: Vector2, move: Vector2) {
        position = pos
        velocity = move
    }
}