package com.github.vshnv.discordsync

import com.github.vshnv.discordsync.messaging.DiscordMessage
import com.github.vshnv.discordsync.messaging.DiscordMessenger
import mineverse.Aust1n46.chat.api.MineverseChatAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import java.util.*
import java.util.stream.Collectors


class EventListener: Listener {
    @EventHandler(ignoreCancelled = true) fun onMessage(event: AsyncPlayerChatEvent) {
        if (event.recipients.size <= 1 && Bukkit.getOnlinePlayers().size > 1) return
        val p: Player = event.getPlayer()
        val chatPlayer = MineverseChatAPI.getOnlineMineverseChatPlayer(p)
        var channel = chatPlayer.currentChannel
        if (chatPlayer.isQuickChat) channel = chatPlayer.quickChannel
        if (channel == null) return
        if (chatPlayer.isPartyChat && !chatPlayer.isQuickChat) return
        if (event.message.startsWith("@")) return
        if (chatPlayer.isMuted(channel.name)) return
        if (channel.hasPermission() && !chatPlayer.player.hasPermission(channel.permission)) return
        if (!channel.isDefaultchannel) return
        DiscordMessenger.sendMessage(
            DiscordMessage(
                event.player.name,
                event.message
            )
        )
    }
    @EventHandler(ignoreCancelled = true) fun onAchieve(event: PlayerAdvancementDoneEvent) {

        if (
            event.player.hasPermission("discordsync.silent") ||
            event.advancement.key.key.contains("recipe/")
         ) return

        try {
            val craftAdvancement =
                event.advancement.javaClass.getMethod("getHandle").invoke(event.advancement)

            val advancementDisplay =
                craftAdvancement.javaClass.getMethod("c").invoke(craftAdvancement)

            val display =
                advancementDisplay.javaClass.getMethod("i").invoke(advancementDisplay) as Boolean

            if (!display) return

        } catch (ex: NullPointerException) {
            return
        } catch (ex: Exception) {
            ex.printStackTrace()
            return
        }

        val rawAdvancementName: String = event.advancement.key.key
        val advancementName = Arrays.stream(
            rawAdvancementName
                .substring(rawAdvancementName.lastIndexOf("/") + 1)
                .toLowerCase().split("_")
                .toTypedArray()
        )   .map { s: String ->
                s.substring(
                    0,
                    1
                ).toUpperCase() + s.substring(1)
            }
            .collect(Collectors.joining(" "))



        DiscordMessenger.sendMessage(
            DiscordMessage(
                "Achievement",
                format(
                    Format.ACHIEVEMENT,
                    "player" to event.player.name,
                    "achievement" to advancementName)
            )
        )
    }
    @EventHandler(ignoreCancelled = true) fun onDeath(event: PlayerDeathEvent) {
        if(event.entity.hasPermission("discordsync.silent"))return;
        if(event.entity.hasMetadata("NPC"))return;
        DiscordMessenger.sendMessage(
            DiscordMessage(
                "Death",
                format(Format.DEATH, "Player" to event.entity.name)
            )
        )
    }


}