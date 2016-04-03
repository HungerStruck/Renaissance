package net.hungerstruck.renaissance.modules.region

import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.xml.module.RModule
import net.hungerstruck.renaissance.xml.module.RModuleContext
import org.jdom2.Document

/**
 * Parses regions. Kewl rite
 */
class RegionModule(match: RMatch, document: Document, modCtx: RModuleContext) : RModule(match, document, modCtx) {
    init {
        val rootEl = document.rootElement.getChild("regions")
        if (rootEl != null) {
            modCtx.regionParser.parseChildren(rootEl)
        }

        println("For ${match.map.mapInfo.name}: parsed ${modCtx.regionManager.count()} regions")
    }
}