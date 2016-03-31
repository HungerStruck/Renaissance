package net.hungerstruck.renaissance.modules.ux

import org.bukkit.DyeColor
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework

import java.util.Random

/**
 * Created by teddy on 31/03/2016.
 */
class RFirework(private var power: Int, private var effect: FireworkEffect?) {

    fun getEffect(): FireworkEffect {
        return effect!!
    }

    fun setEffect(effect: FireworkEffect): RFirework {
        this.effect = effect
        return this
    }

    fun getPower(): Int {
        return power
    }

    fun setPower(power: Int): RFirework {
        this.power = power
        return this
    }

    fun play(location: Location): Firework {
        val firework = location.world.spawnEntity(location, EntityType.FIREWORK) as Firework

        val meta = firework.fireworkMeta
        meta.addEffect(effect)
        meta.power = power
        firework.fireworkMeta = meta
        return firework
    }

    companion object {

        private var random: Random? = null

        init {
            random = Random()
        }

        fun playRandom(location: Location): Firework {
            return RFirework(random!!.nextInt(2) + 1, randomEffect).play(location)
        }

        val randomEffect: FireworkEffect
            get() = FireworkEffect.builder().flicker(random!!.nextBoolean()).with(FireworkEffect.Type.values()[random!!.nextInt(FireworkEffect.Type.values().size)]).withColor(DyeColor.values()[random!!.nextInt(DyeColor.values().size)].color).withFade(DyeColor.values()[random!!.nextInt(DyeColor.values().size)].color).trail(random!!.nextBoolean()).build()
    }


}
