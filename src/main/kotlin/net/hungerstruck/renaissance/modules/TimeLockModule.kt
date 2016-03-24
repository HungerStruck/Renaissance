package net.hungerstruck.renaissance.modules

import net.hungerstruck.renaissance.event.match.RMatchLoadEvent
import net.hungerstruck.renaissance.event.match.RMatchStartEvent
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import net.hungerstruck.renaissance.xml.toBool
import org.bukkit.event.EventHandler
import org.jdom2.Document

/**
 * Created by molenzwiebel on 21-12-15.
 */
class TimeLockModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {
    val locked: Boolean

    init {
        locked = document.rootElement?.getChild("timelock")?.textNormalize.toBool(defaultValue = false)
    }

    @EventHandler
    public fun onMatchLoad(event: RMatchLoadEvent) {
        event.match.world.setGameRuleValue("doDaylightCycle", locked.toString());
    }
}