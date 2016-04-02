package net.hungerstruck.renaissance.lobby

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.RPlayerState
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.event.lobby.RLobbyEndEvent
import net.hungerstruck.renaissance.event.player.RPlayerJoinMatchEvent
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.teleportable
import net.hungerstruck.renaissance.xml.RMap
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.ChatColor.*
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.scheduler.BukkitScheduler

/**
 * Manages a simple match-specific lobby.
 *
 * Created by molenzwiebel on 22-12-15.
 */
class RLobby {
    val id: Int
    val lobbyWorld: World
    val lobbyMap: RMap
    val nextMap: RMap
    val match: RMatch

    constructor(id: Int, lobbyWorld: World, lobbyMap: RMap, nextMap: RMap) {
        this.id = id
        this.lobbyWorld = lobbyWorld
        this.lobbyMap = lobbyMap
        this.nextMap = nextMap

        this.match = Renaissance.matchManager.constructMatch(nextMap)
    }

    val members: Collection<RPlayer>
        get() = RPlayer.getPlayers() { it.lobby == this }

    public fun join(player: RPlayer) {
        if (player.match != null || player.lobby != null) throw IllegalArgumentException("Player is already in match or lobby")

        player.lobby = this
        player.previousState = RPlayerState.create(player)
        player.reset()
        player.gameMode = GameMode.SURVIVAL

        player.teleport(lobbyWorld.spawnLocation.teleportable)

        updateInformation()

        Bukkit.getScheduler().runTaskLater(Renaissance.plugin, {
            for (player2 in lobbyWorld.spawnLocation.world.players) {
                player2.showPlayer(player)
                player.showPlayer(player2)
            }
        }, 2)

        sendMessage("${ChatColor.GREEN}${player.displayName} ${ChatColor.GRAY}has joined the match!")

        if (members.size >= RConfig.Lobby.minimumPlayerStartCount && members.size <= RConfig.Lobby.maximumPlayerStartCount && RConfig.Lobby.autoStart) {
            startCountdown()
        }
    }

    public fun startCountdown() {
        if (!Renaissance.countdownManager.hasCountdown(RLobbyEndCountdown::class.java)) {
            Renaissance.countdownManager.start(RLobbyEndCountdown(this), RConfig.Lobby.countdownTime)
        }
    }

    private fun updateInformation() {
        for (player in members) {
            player.actionBarMessage = "${if (lobbyMap.mapInfo.lobbyProperties!!.canTakeDamage) "${GREEN}PVP $GRAY| " else ""}${if (lobbyMap.mapInfo.lobbyProperties!!.canBuild) "${GREEN}Building $GRAY| " else ""}$YELLOW${members.size}/${RConfig.Lobby.maximumPlayerStartCount} players"
        }
    }

    public fun end() {
        Bukkit.getPluginManager().callEvent(RLobbyEndEvent(this))

        for (player in members) {
            player.lobby = null
            player.match = match
            Bukkit.getPluginManager().callEvent(RPlayerJoinMatchEvent(player, match))
        }

        assert(members.size == 0, { "Still players left in lobby after end." })
        Renaissance.lobbyManager.unloadLobby(this)
    }

    public fun sendMessage(msg: String) {
        Bukkit.getConsoleSender().sendMessage("[lobby-$id] $msg")
        members.forEach { it.sendMessage(RConfig.General.mainMessagePrefix + msg) }
    }
}
