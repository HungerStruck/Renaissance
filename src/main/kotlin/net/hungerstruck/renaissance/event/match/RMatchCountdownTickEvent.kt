package net.hungerstruck.renaissance.event.match

import net.hungerstruck.renaissance.match.RMatch

class RMatchCountdownTickEvent(match: RMatch, val timeLeft: Int) : StruckMatchEvent(match)
