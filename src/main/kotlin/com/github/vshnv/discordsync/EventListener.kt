package com.github.vshnv.discordsync

import com.github.vshnv.discordsync.messaging.DiscordMessage
import com.github.vshnv.discordsync.messaging.DiscordMessenger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent


class EventListener: Listener {
    @EventHandler(ignoreCancelled = true) fun onMessage(event: AsyncPlayerChatEvent) {
        DiscordMessenger.sendMessage(
            DiscordMessage(
                event.player.name,
                event.message
            )
        )
    }
    @EventHandler(ignoreCancelled = true) fun onAchieve(event: PlayerAdvancementDoneEvent) {
        DiscordMessenger.sendMessage(
            DiscordMessage(
                "Achievement",
                format(Format.ACHIEVEMENT, "Name" to event.advancement.key.key)
            )
        )
    }
    @EventHandler(ignoreCancelled = true) fun onDeath(event: PlayerDeathEvent) {
        DiscordMessenger.sendMessage(
            DiscordMessage(
                "Death",
                format(Format.DEATH, "Player" to event.entity.name)
            )
        )
    }


}