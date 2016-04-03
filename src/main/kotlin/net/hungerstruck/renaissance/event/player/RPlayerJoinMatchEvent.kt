package net.hungerstruck.renaissance.event.player

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.match.RMatch

class RPlayerJoinMatchEvent(player: RPlayer, val match: RMatch) : RPlayerEvent(player)