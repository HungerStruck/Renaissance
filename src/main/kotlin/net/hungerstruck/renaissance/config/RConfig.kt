package net.hungerstruck.renaissance.config

import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * Bukkit config ftw.
 *
 * Created by molenzwiebel on 22-12-15.
 */
object RConfig {
    val configFile: File = File("plugins/Renaissance/config.yml")
    val config: FileConfiguration = YamlConfiguration.loadConfiguration(configFile)

    object General {
        var noMatchesMessage: String by path("general.no-matches", "§cThere are no lobbies or matches available.\n\nRejoin later!")
    }

    object Maps {
        var mapDir: String by path("maps.dir", "maps")
        var mapFileName: String by path("maps.map-file", "map.xml")
        var worldPrefix: String by path("maps.world-prefix", "match-")
    }

    object Lobby {
        var defaultLobby: String by path("lobby.default-lobby-map")
        var joinStrategy: JoinStrategy by path("lobby.join-strategy", { JoinStrategy.valueOf(it) }, { it.name })

        var chatFormat: String by path("lobby.chat-format", "<%s> %s")

        var autoStart: Boolean by path("lobby.auto-start", true)
        var minimumPlayerStartCount: Int by path("lobby.minimum-players", 2)
        var maximumPlayerStartCount: Int by path("lobby.maximum-players", 24)

        var tickMessage: String by path("lobby.start-countdown.message", "§2The lobby will end in ${ChatColor.RED}%s §2seconds")
        var countdownTime: Int by path("lobby.start-countdown.time", 20)
    }

    object Match {
        var joinStrategy: JoinStrategy by path("match.join-strategy", { JoinStrategy.valueOf(it) }, { it.name })

        var tickMessage: String by path("match.start-countdown.message", "§2The games will begin in ${ChatColor.RED}%s §2seconds")
        var countdownTime: Int by path("match.start-countdown.time", 20)

        var feastTime: Int by path("match.feast.time", 300)
        var feastRarityIncrease: Double by path("match.feast.rarity-increase", 0.15)
        var feastMessage: String by path("match.feast.message", "§bThere has been a feast!")

        var playerDeathByPlayerMessage: String by path("match.death-player-message", "${ChatColor.RED}%0\$s ${ChatColor.GRAY}was slain by ${ChatColor.RED}%1\$s${ChatColor.GRAY}. \n${ChatColor.RED}%2\$d ${ChatColor.AQUA}players remain.")
        var playerDeathByOtherMessage: String by path("match.death-other-message", "${ChatColor.RED}%0\$s ${ChatColor.GRAY}mysteriously died. \n${ChatColor.RED}%2\$d ${ChatColor.AQUA}players remain.")
        var matchEndList : List<String> by path("math.end-message-list", arrayListOf("${ChatColor.DARK_PURPLE} # # # # # # # # # # # #", "${ChatColor.DARK_PURPLE}# # ${ChatColor.GOLD}   Game Over!   ${ChatColor.DARK_PURPLE}  # #", "${ChatColor.DARK_PURPLE}# # " + "%s" + " wins!" + ChatColor.DARK_PURPLE + " # #", "${ChatColor.DARK_PURPLE} # # # # # # # # # # # #"))
    }

    object Thirst {
        var healthDamage: Int by path("thirst.health-damage", 1)
        var hungerDamage: Int by path("thirst.hunger-damage", 0)
    }

    object Chat {
        var radius: Int by path("chat.radius", 30)
    }

    /**
     * The strategy for which a player gets assigned a lobby/match when he/she first joins.
     * <b>NOTE:</b> This is only for _joining_, not after a cycle. After a cycle, the player will join the next lobby.
     */
    enum class JoinStrategy {
        /**
         * The first lobby or map in the manager will be the one joined.
         */
        FIRST,
        /**
         * A random lobby/map will be selected.
         */
        RANDOM,
        /**
         * The lobby/map with the lowest amount of participants will be selected.
         */
        SMALLEST;
    }
}