package net.hungerstruck.renaissance.match

import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.countdown.Countdown
import net.hungerstruck.renaissance.event.match.RMatchCountdownTickEvent
import org.bukkit.Bukkit

class RMatchStartCountdown(val match: RMatch) : Countdown() {
    override fun onTick(timeLeft: Int) {
        val status = RConfig.Match.tickMessage.format(timeLeft)

        if (timeLeft % 10 == 0 || timeLeft <= 5) {
            match.sendMessage(status)
            Bukkit.getPluginManager().callEvent(RMatchCountdownTickEvent(match, timeLeft))
        }
    }

    override fun onFinish() {
        match.startMatch()
    }
}