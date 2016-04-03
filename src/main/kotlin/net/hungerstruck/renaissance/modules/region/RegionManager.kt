package net.hungerstruck.renaissance.modules.region

import net.hungerstruck.renaissance.util.ContextStore

/**
 * Manages regions. Duh
 */
class RegionManager : ContextStore<RRegion>() {
    val regionParser = RegionParser(this)
}