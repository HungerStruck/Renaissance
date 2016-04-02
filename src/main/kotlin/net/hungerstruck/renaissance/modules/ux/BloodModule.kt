package net.hungerstruck.renaissance.modules.ux

import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.jdom2.Document

/**
 * Created by teddy on 01/04/2016.
 */
class BloodModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {
    private val particle: RParticle

    init {
        this.particle = RParticle(RParticleType.BLOCK_CRACK, false, match.world.spawnLocation, 0.25f, 0.3f, 0.25f, 0.05f, 30).setData(Material.REDSTONE_BLOCK.id)
        registerEvents()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerHit(event: EntityDamageEvent) {
        if (event.isCancelled || !isMatch(event.entity)) return
        particle.setLocation(event.entity.location.add(0.0, 1.0, 0.0)).play(particle.getPlayers(match))
    }
}
