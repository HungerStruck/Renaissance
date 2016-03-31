package net.hungerstruck.renaissance.modules.ux

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.event.match.RMatchCountdownTickEvent
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.jdom2.Document
import java.util.*

/**
 * Created by teddy on 31/03/2016.
 */
class ParticleModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {

    private val random: Random

    init {
        registerEvents()
        this.random = Random()
    }

    @EventHandler
    fun onMatchCountdownTick(event: RMatchCountdownTickEvent) {
        if (event.timeLeft <= 5) {
            val players = getPlayers(event.match)
            for (player in event.match.alivePlayers) {
                RParticle(RParticleType.RED_DUST, player.location.add(0.0, 2.0, 0.0)).playRGB(players, random.nextInt(255), random.nextInt(255), random.nextInt(255))
            }
        }
    }

    private fun getPlayers(match: RMatch): List<Player> {
        val players: MutableList<Player> = arrayListOf()

        for (rplayer in match.players){
            players.add(Bukkit.getPlayer(rplayer.uniqueId))
        }

        return players
    }
}
