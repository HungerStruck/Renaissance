package net.hungerstruck.renaissance.settings

import me.anxuiz.settings.Setting
import me.anxuiz.settings.bukkit.PlayerSettingCallback
import net.hungerstruck.renaissance.modules.scoreboard.RScoreboard
import net.hungerstruck.renaissance.modules.scoreboard.ScoreboardModule
import net.hungerstruck.renaissance.rplayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ScoreboardChangeCallback : PlayerSettingCallback() {
    override fun notifyChange(player: Player, setting: Setting, oldValue: Any?, newValue: Any?) {
        if (player.rplayer.match == null) return
        var scoreboardModule: ScoreboardModule = player.rplayer.match!!.moduleContext.getModule<ScoreboardModule>()!!
        if (newValue == true) {
            val scoreboard = RScoreboard("§e§lHungerStruck", player.uniqueId)
            scoreboardModule.setupScoreboard(scoreboard, player.rplayer)
            scoreboard.show()
            scoreboardModule.scoreboardMap.put(player.uniqueId, scoreboard)
        } else {
            player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
            scoreboardModule.scoreboardMap.remove(player.uniqueId)
        }
    }
}