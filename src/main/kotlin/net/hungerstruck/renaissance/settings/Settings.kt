package net.hungerstruck.renaissance.settings

import me.anxuiz.settings.Setting
import me.anxuiz.settings.SettingBuilder
import me.anxuiz.settings.SettingCallbackManager
import me.anxuiz.settings.SettingRegistry
import me.anxuiz.settings.bukkit.PlayerSettings
import me.anxuiz.settings.types.BooleanType
import me.anxuiz.settings.types.EnumType

object Settings {
    val SCOREBOARD_OPTIONS : Setting = SettingBuilder().name("Scoreboard").alias("sb").summary("Information scoreboard").type(BooleanType()).defaultValue(true).get()
    val BLOOD_OPTIONS : Setting = SettingBuilder().name("Blood").summary("Blood particles").type(BooleanType()).defaultValue(true).get()

    fun register() {
        val registry = PlayerSettings.getRegistry()
        val callbacks = PlayerSettings.getCallbackManager()
        registry.register(SCOREBOARD_OPTIONS)
        registry.register(BLOOD_OPTIONS)
        callbacks.addCallback(SCOREBOARD_OPTIONS, ScoreboardChangeCallback())
        callbacks.addCallback(BLOOD_OPTIONS, BloodChangeCallback())
    }
}