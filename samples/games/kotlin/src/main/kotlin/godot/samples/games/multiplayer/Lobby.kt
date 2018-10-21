package godot.samples.games.multiplayer

import godot.*
import godot.core.GD
import godot.core.GodotError
import godot.core.NodePath
import godot.core.Variant

class Lobby : Control() {

    override fun _ready() {
        getTree().connect("network_peer_connected", this, "_playerConnected")
        getTree().connect("network_peer_disconnected", this, "_playerDisconnected")
        getTree().connect("connected_to_server", this, "_connectedOk")
        getTree().connect("connection_failed", this, "_connectedFail")
        getTree().connect("server_disconnected", this, "_serverDisconnected")
        getNode(NodePath("panel/host")).connect("pressed", this, "_onHostPressed")
        getNode(NodePath("panel/join")).connect("pressed", this, "_onJoinPressed")
    }

    fun _playerConnected(arg: Variant) {
        GD.print("_playerConnected")
        val stage = PackedScene from ResourceLoader.load("res://Games/Multiplayer/Scenes/Stage.tscn")

        getTree().getRoot().addChild(stage.instance())
        hide()
    }

    fun _playerDisconnected(arg: Variant) {
        GD.print("_playerDisconnected")
    }

    fun _connectedOk() {
        GD.print("_connectedOk")
    }

    fun _connectedFail() {
        GD.print("_connectedFail")
    }

    fun _serverDisconnected() {
        GD.print("_serverDisconnected")
    }

    fun endGame() {
        if (hasNode(NodePath("/root/Stage"))) {
            getNode(NodePath("/root/Stage")).free()
        }

        getTree().setNetworkPeer(NetworkedMultiplayerENet())
    }

    fun _onHostPressed() {
        val host = NetworkedMultiplayerENet()
        host.setCompressionMode(NetworkedMultiplayerENet.COMPRESS_RANGE_CODER)
        val code = host.createServer(10000, 1)
        if (code != GodotError.OK) {
            GD.print("Creating server error")
            return
        }
        getTree().setNetworkPeer(host)
        GD.print("Waiting for player")
        (Panel from getNode(NodePath("panel"))).hide()
        (Label from getNode(NodePath("wait"))).show()
    }

    fun _onJoinPressed() {
        val host = NetworkedMultiplayerENet()
        val ip = (LineEdit from getNode(NodePath("panel/ip"))).text
        host.setCompressionMode(NetworkedMultiplayerENet.COMPRESS_RANGE_CODER)
        val code = host.createClient(ip, 10000)
        if (code != GodotError.OK) {
            GD.print("Creating client error")
            return
        }
        getTree().setNetworkPeer(host)
        GD.print("Connecting...")
    }
}