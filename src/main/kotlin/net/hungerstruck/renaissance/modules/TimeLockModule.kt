package net.hungerstruck.renaissance.modules

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.event.match.RMatchLoadEvent
import net.hungerstruck.renaissance.event.match.RMatchStartEvent
import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import net.hungerstruck.renaissance.xml.toBool
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.jdom2.Document

class TimeLockModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {
    val locked: Boolean

    init {
        locked = document.rootElement?.getChild("timelock")?.textNormalize.toBool(defaultValue = false)
        registerEvents()
    }

    @EventHandler
    public fun onMatchStartEvent(event: RMatchStartEvent) {
        if (!isMatch(event.match)) return

        // Set dayLightCycle to the opposite of the value from XML (if mapmaker wants it to be locked, true, then cycling should be off, false)
        event.match.world.setGameRuleValue("doDaylightCycle", locked.not().toString());

    }
}