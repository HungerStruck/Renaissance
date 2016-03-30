package net.hungerstruck.renaissance.match

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.config.RConfig
import net.hungerstruck.renaissance.countdown.Countdown
import org.bukkit.Sound

/**
 * Created by molenzwiebel on 01-01-16.
 */
class RMatchStartCountdown(val match: RMatch) : Countdown() {
    override fun onTick(timeLeft: Int) {
        val status = RConfig.Match.tickMessage.format(timeLeft)

        if (timeLeft % 10 == 0 || timeLeft <= 5) {
            match.sendMessage(status)

            for (player: RPlayer in match.alivePlayers){
                player.playSound(player.location, Sound.ANVIL_LAND, 1f, 2f)
                player.playSound(player.location, Sound.ORB_PICKUP, 1f, 0.5f)
            }
        }
    }

    override fun onFinish() {
        match.startMatch()
    }
}