package com.github.vshnv.discordsync.messaging

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.WebhookClientBuilder
import club.minnced.discord.webhook.send.AllowedMentions
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.Webhook
import kotlin.reflect.KProperty

class WebHookDelegate(channel: TextChannel) {

    var client: WebhookClient? = null

    init {
        channel.retrieveWebhooks().queue {
            val hook = it.firstOrNull { it.name.startsWith("DiscordSync") }
            if (hook == null) {
                initializeNewHook(channel)
            } else {
                prepareClient(hook)
            }
        }
    }

    operator fun getValue(ref: Any?, prop: KProperty<*>) = client

    /**
     * Creates a webhook and prepares client once ready
     */
    private fun initializeNewHook(channel: TextChannel) {
        channel.createWebhook("DiscordSync-${channel.name}").queue {
            prepareClient(it)
        }
    }

    /**
     * Prepares client with specified webhook
     * @param hook the webhook for the client to wrap
     */
    private fun prepareClient(hook: Webhook) {
        client = WebhookClientBuilder(hook.url).setAllowedMentions(AllowedMentions.none()).build()
    }
}