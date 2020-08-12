package com.github.vshnv.discordsync

import club.minnced.jda.reactor.ReactiveEventManager
import club.minnced.jda.reactor.on
import com.github.vshnv.discordsync.messaging.*
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import org.bukkit.Bukkit
import java.time.Duration


internal class Bot(token: String) {
    private val listeningChannels = mutableSetOf<Long>()
    private val jda: JDA


    init {
        val builder = JDABuilder.createLight(token, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES)
            .setActivity(Activity.streaming("Inter-Server Messages","https://github.com/Vshnv"))
            .setStatus(OnlineStatus.DO_NOT_DISTURB)
        jda = builder.build()
        MCMessenger.registerReceiver(MCReceiver())
        jda.addEventListener(object : ListenerAdapter() {
            override fun onMessageReceived(event: MessageReceivedEvent) {
                if (!listeningChannels.contains(event.channel.idLong)) return
                if (event.author.isBot || event.isWebhookMessage) return

                if (event.message.contentStripped.equals("=playerList", true)) {
                    event.message.delete().queue()
                    event.channel.sendMessage(
                        EmbedBuilder()
                            .addField("Player Count", Bukkit.getOnlinePlayers().size.toString(),false)
                            .addField("Online Players", Bukkit.getOnlinePlayers().joinToString { ", " }, false)
                            .addField("Requested by", event.message.author.name, false)
                            .build()
                    ).queue {
                        it.delete().delay(Duration.ofSeconds(10)).queue()
                    }
                    return
                }

                MCMessenger.sendMessage(MCMessage(event.author.name, event.message.contentStripped, Format.CHAT))
                DiscordMessenger.sendMessage(DiscordMessage(event.author.name, event.message.contentStripped, event.author.avatarUrl?: event.author.defaultAvatarUrl, event.channel.idLong))
            }
        })
        jda.awaitReady()
    }

    /**
     * Registers a channel to listen to
     */
    fun registerListener(id: Long) {
        listeningChannels.add(id)
    }

    /**
     * Unregisters a channel from classes to listen to
     */
    fun unregisterListener(id: Long) {
        listeningChannels.remove(id)
    }

    /**
     * Unregisters all channels as to be listened to
     */
    fun unregisterAllListener() {
        listeningChannels.clear()
    }

    /**
     * Returns instance of textchannel from JDA
     */
    fun fetchChannel(id: Long): TextChannel? {
        return jda.getTextChannelById(id)
    }

    /**
     * Shutsdown JDA bot
     */
    fun shutdown() {
        jda.shutdown()
    }
}