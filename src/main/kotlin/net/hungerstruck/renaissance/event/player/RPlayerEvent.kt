package net.hungerstruck.renaissance.event.player

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.event.StruckEvent

abstract class RPlayerEvent(val player: RPlayer) : StruckEvent()