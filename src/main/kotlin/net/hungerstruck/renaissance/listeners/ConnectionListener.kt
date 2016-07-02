package net.hungerstruck.renaissance.listeners

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.event.player.RPlayerJoinMatchEvent
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.rplayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerListPingEvent
import org.bukkit.permissions.PermissionAttachment
import java.util.*

/**
 * Handles connections. Basically just assigns a player to a lobby or match.
 *
 * Created by molenzwiebel on 01-01-16.
 */
class ConnectionListener : Listener {

    @EventHandler
    public fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent)  {
        if(     (
                event.getMessage().startsWith("/tell")
                || event.getMessage().startsWith("/say")
                || event.getMessage().startsWith("/me")
                || event.getMessage().startsWith("/minecraft:tell")
                || event.getMessage().startsWith("/minecraft:say")
                || event.getMessage().startsWith("/minecraft:me")
                ) && !event.player.isOp) {
            event.player.sendMessage("${ChatColor.RED} This command has been disabled to preserve competitive integrity")
            event.isCancelled = true
        }
    }

    @EventHandler
    public fun onServerListPing(event: ServerListPingEvent) {
        val sb = StringBuilder()
        val match = Renaissance.matchManager.findMatch(RConfig.Match.joinStrategy)
        sb.append(ChatColor.RED.toString())
          .append("@Hunger")
          .append(ChatColor.WHITE.toString())
          .append("StruckHQ")
          .append(ChatColor.GRAY.toString())
          .append(" [1.8]\n")
          .append(ChatColor.RED.toString())
          .append("Now ${match!!.state.toString().toLowerCase()}: ")
          .append(ChatColor.WHITE.toString())
          .append("${match.map.mapInfo.name}")
    }

    @EventHandler
    public fun onPlayerConnect(event: PlayerJoinEvent) {
        val lobby = Renaissance.lobbyManager.findLobby(RConfig.Lobby.joinStrategy)
        event.joinMessage = null

        if (lobby != null) {
            lobby.join(event.player.rplayer)
            return
        }

        val match = Renaissance.matchManager.findMatch(RConfig.Match.joinStrategy)
        if (match != null) {
            event.player.rplayer.match = match
            Bukkit.getPluginManager().callEvent(RPlayerJoinMatchEvent(event.player.rplayer, match))
            return
        }

        event.player.kickPlayer(RConfig.General.noMatchesMessage)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (event.player.rplayer.match != null) {
            val match = event.player.rplayer.match

            if (match?.state == RMatch.State.PLAYING && event.player.rplayer.state == RPlayer.State.PARTICIPATING) {
                event.player.health = 0.0
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerKick(event: PlayerKickEvent) {
        if (event.player.rplayer.match != null) {
            val match = event.player.rplayer.match

            if (match?.state == RMatch.State.PLAYING && event.player.rplayer.state == RPlayer.State.PARTICIPATING) {
                event.player.health = 0.0
            }
        }
    }
}