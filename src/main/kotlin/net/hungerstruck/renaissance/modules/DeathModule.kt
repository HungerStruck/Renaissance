package net.hungerstruck.renaissance.modules

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.getIgnoreBounds
import net.hungerstruck.renaissance.getRPlayer
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.xml.module.Dependencies
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.vehicle.VehicleDamageEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.inventory.ItemStack
import org.jdom2.Document
import java.util.*

/**
 * Handles player death, respawning and observer interaction prevention.
 *
 * Created by molenzwiebel on 03-01-16.
 */
@Dependencies
class DeathModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {
    private val watching: WeakHashMap<RPlayer, Int> = WeakHashMap()

    init {
        registerEvents()
    }

    private fun isSpectator(p: Player): Boolean {
        return p.getRPlayer().match == match && (p.getRPlayer().state == RPlayer.State.SPECTATING || p.getRPlayer().match!!.state != RMatch.State.PLAYING)
    }

    // ===========================================================
    // ================= Death and Respawning ====================
    // ===========================================================

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (!isMatch(event.entity)) return
        if (match.state != RMatch.State.PLAYING) return

        val victim = event.entity.getRPlayer()
        victim.state = RPlayer.State.SPECTATING
        victim.spigot().collidesWithEntities = false
        victim.allowFlight = true

        if (match.alivePlayers.size != 1) {
            // Still players alive.
            match.sendMessage("${victim.displayName} died. ${match.alivePlayers.size} players remain.")
        } else {
            // We have a winner.
            match.sendMessage("${match.alivePlayers[0].displayName} won!")
            match.endMatch()
            RPlayer.updateVisibility()
        }

        match.world.strikeLightningEffect(victim.location)
        event.deathMessage = null
        RPlayer.updateVisibility()
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        if (!isSpectator(event.player)) return

        event.respawnLocation = match.world.spawnLocation
        event.player.inventory.setItem(0, ItemStack(Material.COMPASS, 1))
    }

    // ===========================================================
    // ================ Spectator Teleportation ==================
    // ===========================================================

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        if (match.state != RMatch.State.PLAYING || !isMatch(e.player) || e.player.getRPlayer().state != RPlayer.State.SPECTATING) return

        if (e.player.itemInHand.type == Material.COMPASS) {
            if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
                val prev = (watching[e.player.getRPlayer()] ?: 0) - 1
                watching[e.player.getRPlayer()] = prev

                e.player.teleport(match.alivePlayers.getIgnoreBounds(prev))
            }

            if (e.action == Action.LEFT_CLICK_BLOCK || e.action == Action.LEFT_CLICK_AIR) {
                val next = (watching[e.player.getRPlayer()] ?: 0) + 1
                watching[e.player.getRPlayer()] = next

                e.player.teleport(match.alivePlayers.getIgnoreBounds(next))
            }
        }
    }

    // ===========================================================
    // ================ Interaction Cancelling ===================
    // ===========================================================

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        if (event.entity is Player) {
            event.isCancelled = isSpectator(event.entity as Player)
        }
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        event.isCancelled = isSpectator(event.player)
    }

    @EventHandler
    fun onItemPickup(event: PlayerPickupItemEvent) {
        event.isCancelled = isSpectator(event.player)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        event.isCancelled = isSpectator(event.player)
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        event.isCancelled = isSpectator(event.player)
    }

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        if (event.target is Player && isSpectator(event.target as Player)) {
            event.target = null
        }
    }

    @EventHandler
    fun onEntityDamageByEntityEvent(event: EntityDamageByEntityEvent) {
        if (event.entity is Player && isSpectator(event.entity as Player)) {
            event.isCancelled = true
        } else if (event.damager is Player && isSpectator(event.damager as Player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity.type != EntityType.PLAYER) return
        if (event.isCancelled) return
        event.isCancelled = isSpectator(event.entity as Player)
    }

    @EventHandler
    fun onInteractEvent(event: PlayerInteractEvent) {
        event.isCancelled = isSpectator(event.player)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) return

        val player = (event.whoClicked as Player).getRPlayer()
        if (player.match == null || player.match != match) return

        if (match.state == RMatch.State.PLAYING || match.state == RMatch.State.STARTING) {
            if (isSpectator(player)) {
                if (event.inventory.type != InventoryType.PLAYER) event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        if (isSpectator(e.player) && e.action == Action.RIGHT_CLICK_BLOCK) {
            val blockState = e.clickedBlock.state
            if (blockState is Chest) {
                val player = e.player
                val initInventory = blockState.inventory
                val dupeInventory = Bukkit.createInventory(player, initInventory.size, "Spectating")
                dupeInventory.contents = initInventory.contents.copyOf()
                player.openInventory(dupeInventory)
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onVehicleDamage(event: VehicleDamageEvent) {
        if (event.attacker is Player) {
            event.isCancelled = isSpectator(event.attacker as Player)
        }
    }

    @EventHandler
    fun onVehicleDestroy(event: VehicleDestroyEvent) {
        if (event.attacker is Player) {
            event.isCancelled = isSpectator(event.attacker as Player)
        }
    }

    @EventHandler
    fun onVehicleEnter(event: VehicleEnterEvent) {
        if (event.entered is Player) {
            event.isCancelled = isSpectator(event.entered as Player)
        }
    }
}