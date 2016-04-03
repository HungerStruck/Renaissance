package net.hungerstruck.renaissance.event.player

import net.hungerstruck.renaissance.RPlayer

class RPlayerThirstUpdateEvent(player: RPlayer, val thirst: Int) : RPlayerEvent(player)
