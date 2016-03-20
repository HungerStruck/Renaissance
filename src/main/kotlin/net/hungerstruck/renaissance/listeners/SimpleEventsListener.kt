package net.hungerstruck.renaissance.listeners;

import net.hungerstruck.renaissance.Renaissance
import net.hungerstruck.renaissance.match.RMatch
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.weather.WeatherChangeEvent

class SimpleEventsListener : Listener {
    @EventHandler
    public fun onCreatureSpawn(event: CreatureSpawnEvent) {
        val match = getMatchByWorld(event.entity.world) ?: return
        if(match.state == RMatch.State.LOADED)
            event.isCancelled = true;
    }

    @EventHandler
    public fun onWeatherChange(event: WeatherChangeEvent) {
        val match = getMatchByWorld(event.world) ?: return
        if(match.state == RMatch.State.LOADED)
            event.isCancelled = true;
    }

    private fun getMatchByWorld(world: World) : RMatch? {
        return Renaissance.matchManager.matches.get(world)
    }
}
