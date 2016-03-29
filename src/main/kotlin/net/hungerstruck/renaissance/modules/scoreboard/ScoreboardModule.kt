package net.hungerstruck.renaissance.modules.scoreboard

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.RenaissancePlugin
import net.hungerstruck.renaissance.event.match.RMatchEndEvent
import net.hungerstruck.renaissance.event.match.RMatchLoadEvent
import net.hungerstruck.renaissance.event.match.RMatchStartEvent
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.rplayer
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.jdom2.Document

import java.util.HashMap
import java.util.UUID

/**
 * Created by teddy on 29/03/2016.
 */
class ScoreboardModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {

    val scoreboardMap: MutableMap<UUID, RScoreboard>
    var timer = 0

    init {
        scoreboardMap = HashMap<UUID, RScoreboard>()
        registerEvents(this)
    }

    @EventHandler
    fun onMatchStart(event: RMatchStartEvent) {
        for (p in event.match.players) {
            val scoreboard = RScoreboard("§e§lHungerStruck", p.uniqueId)
            setupScoreboard(scoreboard)
            scoreboard.show()
            scoreboardMap.put(p.uniqueId, scoreboard)
        }

        timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Renaissance.plugin, ScoreboardTimer(this), 0, 20)
    }

    @EventHandler
    fun onMatchEnd(event: RMatchEndEvent){
        for (p in event.match.players){
            p.player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        }

        scoreboardMap.clear()
        Bukkit.getScheduler().cancelTask(timer)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDeath(event: PlayerDeathEvent){
        if(match.players.contains(event.entity.rplayer)){
            for(scoreboard in scoreboardMap.values){
                scoreboard.setScore(-6, (match.alivePlayers.size-1).toString()).show()
            }
        }
    }

    private fun setupScoreboard(scoreboard: RScoreboard) {
        scoreboard.setScore(-1, "§1 ").setScore(-2, "§6§lTime").setScore(-3, "00:00")
        scoreboard.setScore(-4, "§2 ").setScore(-5, "§3§lAlive").setScore(-6, this.match.alivePlayers.size.toString())
        scoreboard.setScore(-7, "§3 ").setScore(-8, "§5§lServer").setScore(-9, "server")
    }
}
