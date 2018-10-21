package godot.samples.games.multiplayer

import godot.*
import godot.core.GD
import godot.core.NodePath

class Stage : Node() {

    override fun _ready() {

        if (getTree().isNetworkServer()) {
            getNode(NodePath("player2")).setNetworkMaster(getTree().getNetworkConnectedPeers()[0].toLong())
        } else {
            getNode(NodePath("player2")).setNetworkMaster(getTree().getNetworkUniqueId())
        }

        GD.print("Unique ID: ${getTree().getNetworkUniqueId()}")
    }
}