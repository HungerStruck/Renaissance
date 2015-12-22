package net.hungerstruck.renaissance.match

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.xml.RMap
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.bukkit.World

/**
 * Represents a match.
 *
 * Created by molenzwiebel on 20-12-15.
 */
class RMatch {
    val map: RMap
    val world: World
    var state: State = State.LOADED

    private val moduleContext: RModuleContext

    val players: Collection<RPlayer>
        get() = RPlayer.getPlayers() { it.match == this }

    constructor(map: RMap, world: World) {
        this.map = map
        this.world = world

        this.moduleContext = RModuleContext(this, map.document)
    }

    /**
     * Performs any unloading and cleanup that this map might want to do.
     */
    public fun cleanup() {
        for (module in moduleContext.modules) {
            module.cleanup()
        }
    }

    public enum class State {
        // Loaded. Players might be in already, but countdown for start is not yet running.
        LOADED,
        // Countdown for start is running.
        STARTING,
        // The match is currently in progress.
        PLAYING,
        // The match has ended but has not yet been unloaded. When unloaded, the RMatch gets gcd.
        ENDED;
    }
}