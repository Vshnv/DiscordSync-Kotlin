package com.github.vshnv.discordsync

import com.github.vshnv.discordsync.messaging.DiscordMessenger
import com.github.vshnv.discordsync.messaging.DiscordReceiver
import org.bukkit.plugin.java.JavaPlugin
import java.lang.IllegalArgumentException
import java.text.Normalizer
import java.util.*


/**
 * Unregisters present listeners and receivers and load values from config
 * @param plugin instance of the plugin class
 */
internal fun reloadReceiversFromConfig(plugin: DSync) {
    plugin.reloadConfig()
    val config = plugin.config
    val bot = plugin.bot
    bot.unregisterAllListener()
    DiscordMessenger.unregisterAllReceivers()
    config.getLongList("discord.channels").forEach {
        bot.fetchChannel(it)?.let { channel ->
            DiscordMessenger.registerReceiver(
                DiscordReceiver(channel)
            )
        }
        bot.registerListener(it)
    }
}
private val formatMap = mutableMapOf<Format, String>()
internal fun loadFormats(plugin: DSync) {
    plugin.reloadConfig()
    val config = plugin.config

    Format.values().forEach {
        formatMap[it] = getFormat(it, plugin)
    }
}

/**
 * Fetches token from config
 * @param plugin instance of the plugin class
 */
internal fun loadToken(plugin: DSync): String {
    plugin.reloadConfig()
    val config = plugin.config
    return config.getString("discord.token") ?: throw IllegalArgumentException("Token not provided!")
}


/**
 * Retrieves formats from plugin for messages.
 *
 */
private fun getFormat(format: Format, plugin: DSync): String {
    val config = plugin.config
    return config.getString(format.path) ?: ""
}


fun format(format: Format, vararg args: Pair<String, String>): String {
    var result = formatMap[format] ?: ""
    args.forEach {
        val placeholder = "%${it.first}%"
        result = result.replace(placeholder, it.second)
    }
    return result
}




enum class Format(val path: String){
    D_CAST("formats.minecraft.dcast"),
    ACHIEVEMENT("formats.discord.achievement"),
    DEATH("formats.discord.death"),
    CHAT("formats.minecraft.chat")
}