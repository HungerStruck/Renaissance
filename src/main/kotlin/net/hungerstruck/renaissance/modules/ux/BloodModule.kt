package net.hungerstruck.renaissance.modules.ux

import me.anxuiz.settings.bukkit.PlayerSettings
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.settings.BloodOptions
import net.hungerstruck.renaissance.settings.Settings
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.jdom2.Document

class BloodModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {
    private val particle: RParticle

    init {
        this.particle = RParticle(RParticleType.BLOCK_CRACK, false, match.world.spawnLocation, 0.25f, 0.3f, 0.25f, 0.05f, 30).setData(Material.REDSTONE_BLOCK.id)
        registerEvents()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerHit(event: EntityDamageEvent) {
        if (event.isCancelled || !isMatch(event.entity)) return

        val matchPlayers: MutableList<Player> = arrayListOf()
        matchPlayers.addAll(match.players.map {it.bukkit}.filter { PlayerSettings.getManager(it).getValue(Settings.BLOOD_OPTIONS, BloodOptions::class.java) == BloodOptions.ON })
        particle.setLocation(event.entity.location.add(0.0, 1.0, 0.0)).play(matchPlayers)

        if(event is EntityDamageByEntityEvent) {
            if(event.getDamager().getType() == EntityType.ARROW) {
                for(player in matchPlayers)
                    player.playEffect(event.getEntity().getLocation(), Effect.STEP_SOUND, 10);
            }
            if(event.getDamager() is Player) {
                (event.damager as Player).playSound((event.damager as Player).getLocation(), Sound.HURT_FLESH, 200.0F, 200.0F)
            }
        }
    }
}
