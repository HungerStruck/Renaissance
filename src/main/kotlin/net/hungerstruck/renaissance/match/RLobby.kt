package net.hungerstruck.renaissance.match

import net.hungerstruck.renaissance.RPlayer
import net.hungerstruck.renaissance.RPlayerState

/**
 * Manages a simple server-wide lobby.

 * Created by molenzwiebel on 22-12-15.
 */
class RLobby {
    val members: Collection<RPlayer>
        get() = RPlayer.getPlayers() { it.lobby == this }

    public fun join(player: RPlayer) {
        if (player.match != null || player.lobby != null) throw IllegalArgumentException("Player is already in match or lobby")

        player.lobby = this
        player.previousState = RPlayerState.create(player.bukkit)
        player.reset()

        //FIXME: Start countdown once enough players are in
    }
}
