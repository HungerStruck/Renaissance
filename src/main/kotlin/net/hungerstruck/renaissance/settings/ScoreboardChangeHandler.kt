package net.hungerstruck.renaissance.settings

import me.anxuiz.settings.Setting
import me.anxuiz.settings.SettingCallback
import me.anxuiz.settings.SettingManager
import me.anxuiz.settings.bukkit.PlayerSettingCallback
import me.anxuiz.settings.util.TypeUtil
import net.hungerstruck.renaissance.modules.scoreboard.RScoreboard
import net.hungerstruck.renaissance.modules.scoreboard.ScoreboardModule
import net.hungerstruck.renaissance.rplayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ScoreboardChangeCallback : PlayerSettingCallback() {
    override fun notifyChange(player: Player, setting: Setting, oldValue: Any?, newValue: Any?) {
        var scoreboardModule: ScoreboardModule = player.rplayer.match!!.moduleContext.getModule<ScoreboardModule>()!!
        if (newValue == true) {
            // show scoreboard
            if (player.rplayer.match != null) {
                val scoreboard = RScoreboard("§e§lHungerStruck", player.uniqueId)
                scoreboardModule.setupScoreboard(scoreboard, player.rplayer)
                scoreboard.show()
                scoreboardModule.scoreboardMap.put(player.uniqueId, scoreboard)
            }
        } else {
            // hide scoreboard
            if (player.rplayer.match != null) {
                player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
                scoreboardModule.scoreboardMap.remove(player.uniqueId)
            }
        }
    }
}