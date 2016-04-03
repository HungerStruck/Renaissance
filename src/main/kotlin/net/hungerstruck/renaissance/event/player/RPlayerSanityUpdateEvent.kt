package net.hungerstruck.renaissance.event.player

import net.hungerstruck.renaissance.RPlayer

class RPlayerSanityUpdateEvent(player: RPlayer, val sanity: Int) : RPlayerEvent(player)
