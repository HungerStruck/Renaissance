package net.hungerstruck.renaissance.modules.ux

import co.enviark.struckbukkit.effects.ParticleBuilder
import me.anxuiz.settings.bukkit.PlayerSettings
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.settings.Settings
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.jdom2.Document

class BloodModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {
    private val particle: ParticleBuilder

    init {
        this.particle = Particle.builder().setParticle(Particle.BLOCK_CRACK).setOffset(0.25f).setCount(30).setData(Material.REDSTONE_BLOCK.id)
        registerEvents()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerHit(event: EntityDamageEvent) {
        if (event.isCancelled || !isMatch(event.entity)) return
        match.players.filter { it.getSetting<Boolean>(Settings.BLOOD_OPTIONS) == true }.forEach { it.bukkit.particles().play(particle.setLocation(event.entity.location.add(0.0, 1.0, 0.0))) }
    }
}
