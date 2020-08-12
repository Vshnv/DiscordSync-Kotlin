package com.github.vshnv.discordsync.messaging

import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookMessage
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import com.github.vshnv.discordsync.Message
import com.github.vshnv.discordsync.MessageReceiver
import com.github.vshnv.discordsync.Messenger
import net.dv8tion.jda.api.entities.MessageEmbed

import net.dv8tion.jda.api.entities.TextChannel

const val DISCORD_DEFAULT_AVATAR = "https://discordapp.com/assets/322c936a8c8be1b803cd94861bdfa868.png"


/* Representation of a message to be sent to Discord */
class DiscordMessage(name: String, msg: String, val avatar: String = DISCORD_DEFAULT_AVATAR,val embed: WebhookEmbed? = null, val origin: Long = -1): Message(name, msg)

/* OUT */
/* MC -> Discord messages */
object DiscordMessenger: Messenger<DiscordReceiver, DiscordMessage>()


/* IN */
/* Receives messages to be sent to discord */
class DiscordReceiver(channel: TextChannel): MessageReceiver<DiscordMessage>() {
    private val channelID = channel.idLong
    private val webHook by WebHookDelegate(channel)



    override fun onReceive(msg: DiscordMessage) {
        if (msg.origin == channelID) return // This is the origin channel

        // Ignore call if hook client isn't ready
        webHook?.let {
            val hookBuilder =  WebhookMessageBuilder()
                .append(
                    msg.msg.replace("@","")
                )
                .setAvatarUrl(msg.avatar)
                .setUsername(msg.user)
            if (msg.embed != null) {
                hookBuilder.addEmbeds(msg.embed)
            }
            it.send(hookBuilder.build())
        }

    }


}