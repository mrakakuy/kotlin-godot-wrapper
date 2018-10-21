package godot.samples.games.main

import godot.*
import godot.core.NodePath

class ChooseGameScreen: Node() {

    lateinit var playDodgeButton: Button
    lateinit var playPongButton: Button
    lateinit var playShmupButton: Button
    lateinit var backButton: Button
    lateinit var playCatchBallButton: Button
    lateinit var playFastFinishButton: Button
    lateinit var playMultiplayerButton: Button

    override fun _ready() {
        playMultiplayerButton = (Button from getNode(NodePath("MenuButtons/PlayMultiplayerButton"))).apply {
            connect("pressed", this@ChooseGameScreen, "_onPlayMultiplayerButtonPressed")
        }
        playDodgeButton = (Button from getNode(NodePath("MenuButtons/PlayDodgeButton"))).apply {
            connect("pressed", this@ChooseGameScreen, "_onPlayDodgeButtonPressed")
        }
        playPongButton = (Button from getNode(NodePath("MenuButtons/PlayPongButton"))).apply {
            connect("pressed", this@ChooseGameScreen, "_onPlayPongButtonPressed")
        }
        playShmupButton = (Button from getNode(NodePath("MenuButtons/PlayShmupButton"))).apply {
            connect("pressed", this@ChooseGameScreen, "_onPlayShmupButtonPressed")
        }
        playCatchBallButton = (Button from getNode(NodePath("MenuButtons/PlayCatchBallButton"))).apply {
            connect("pressed", this@ChooseGameScreen, "_onPlayCatchBallButtonPressed")
        }
        playFastFinishButton = (Button from getNode(NodePath("MenuButtons/PlayFastFinishButton"))).apply {
            connect("pressed", this@ChooseGameScreen, "_onPlayFastFinishButtonPressed")
        }
        backButton = (Button from getNode(NodePath("MenuButtons/BackButton"))).apply {
            connect("pressed", this@ChooseGameScreen, "_onBackButtonPressed")
        }
    }

    fun _onPlayMultiplayerButtonPressed() {
        getTree().changeScene("res://Games/Multiplayer/Scenes/Lobby.tscn")
    }

    fun _onPlayDodgeButtonPressed() {
        getTree().changeScene("res://Games/dodge/Scenes/Main.tscn")
    }

    fun _onPlayShmupButtonPressed() {
        getTree().changeScene("res://Games/Shmup/Scenes/Stage.tscn")
    }

    fun _onPlayCatchBallButtonPressed() {
        getTree().changeScene("res://Games/CatchBall/Scenes/Stage.tscn")
    }

    fun _onPlayFastFinishButtonPressed() {
        getTree().changeScene("res://Games/FastFinish/Scenes/Stage.tscn")
    }

    fun _onPlayPongButtonPressed() {
        getTree().changeScene("res://Games/Pong/Scenes/Main.tscn")
    }

    fun _onBackButtonPressed() {
        getTree().changeScene("res://Games/Main/Scenes/MainScreen.tscn")
    }

}