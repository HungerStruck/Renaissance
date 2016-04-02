package net.hungerstruck.renaissance.settings;

import me.anxuiz.settings.Setting;
import me.anxuiz.settings.SettingBuilder;
import me.anxuiz.settings.SettingCallbackManager;
import me.anxuiz.settings.SettingRegistry;
import me.anxuiz.settings.bukkit.PlayerSettings;
import me.anxuiz.settings.types.BooleanType;
import me.anxuiz.settings.types.EnumType;

public class Settings {
	public static final Setting SCOREBOARD_OPTIONS = new SettingBuilder()
													.name("Scoreboard")
													.alias("sb")
													.summary("Information scoreboard")
													.type(new BooleanType())
													.defaultValue(true)
													.get();

	public static void register() {
		SettingRegistry registry = PlayerSettings.getRegistry();
		SettingCallbackManager callbacks = PlayerSettings.getCallbackManager();
		registry.register(SCOREBOARD_OPTIONS);
		callbacks.addCallback(SCOREBOARD_OPTIONS, new ScoreboardChangeCallback());
	}
}