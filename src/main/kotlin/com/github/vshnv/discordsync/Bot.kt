package com.github.vshnv.discordsync

import club.minnced.jda.reactor.on
import com.github.vshnv.discordsync.messaging.DiscordMessage
import com.github.vshnv.discordsync.messaging.DiscordMessenger
import com.github.vshnv.discordsync.messaging.MCMessage
import com.github.vshnv.discordsync.messaging.MCMessenger
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.requests.GatewayIntent

internal class Bot(token: String) {
    private val listeningChannels = mutableSetOf<Long>()
    private val jda = JDABuilder.createLight(token, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS)
        .setActivity(Activity.streaming("Inter-Server Messages","https://github.com/Vshnv"))
        .setStatus(OnlineStatus.DO_NOT_DISTURB)
        .build()


    init {
        jda.on<MessageReceivedEvent>()
            .filter { listeningChannels.contains(it.channel.idLong) }
            .filter{ !it.author.isBot && !it.isWebhookMessage}
            .subscribe {
                MCMessenger.sendMessage(MCMessage(it.author.name, it.message.contentStripped, Format.CHAT))
                DiscordMessenger.sendMessage(DiscordMessage(it.author.name, it.message.contentStripped, it.author.avatarUrl?: it.author.defaultAvatarUrl, it.channel.idLong))
            }
    }

    fun registerListener(id: Long) {
        listeningChannels.add(id)
    }
    fun unregisterListener(id: Long) {
        listeningChannels.remove(id)
    }
    fun unregisterAllListener() {
        listeningChannels.clear()
    }
    fun fetchChannel(id: Long): TextChannel? {
        return jda.getTextChannelById(id)
    }

    fun shutdown() {
        jda.shutdown()
    }
}