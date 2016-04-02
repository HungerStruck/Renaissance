package net.hungerstruck.renaissance.settings;

import me.anxuiz.settings.Setting;
import me.anxuiz.settings.SettingCallback;
import me.anxuiz.settings.SettingManager;
import me.anxuiz.settings.bukkit.PlayerSettingCallback;
import me.anxuiz.settings.util.TypeUtil;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ScoreboardChangeCallback extends PlayerSettingCallback {
    @Override
    public void notifyChange(@Nonnull Player player, @Nonnull Setting setting, @Nonnull Object oldValue, @Nonnull Object newValue) {
        if(TypeUtil.getValue(newValue, Boolean.class)) {
            // show scoreboard
        } else {
            // hide scoreboard
        }
    }
}
