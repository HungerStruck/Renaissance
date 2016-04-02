package net.hungerstruck.renaissance.settings

import me.anxuiz.settings.Setting
import me.anxuiz.settings.bukkit.PlayerSettingCallback
import net.hungerstruck.renaissance.modules.ux.BloodModule
import net.hungerstruck.renaissance.rplayer
import org.bukkit.entity.Player

/**
 * Created by teddy on 02/04/2016.
 */
class BloodChangeCallback : PlayerSettingCallback() {
    override fun notifyChange(player: Player, setting: Setting, oldValue: Any?, newValue: Any?) {
        if (player.rplayer.match == null) return
        var bloodModule = player.rplayer.match!!.moduleContext.getModule<BloodModule>()!!
        if (newValue == true) {
            if (bloodModule.noBlood.contains(player)) bloodModule.noBlood.remove(player)
        } else {
            if (!bloodModule.noBlood.contains(player)) bloodModule.noBlood.add(player)
        }
    }
}