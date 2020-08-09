package com.github.vshnv.discordsync

import com.github.vshnv.discordsync.command.registerAllCommands
import org.bukkit.plugin.java.JavaPlugin

class DSync: JavaPlugin() {
    internal lateinit var bot: Bot
        private set
    override fun onEnable() {
        registerAllCommands()
        bot = Bot(loadToken(this))
        loadFormats(this)
        this.server.pluginManager.registerEvents(EventListener(), this)
    }

    override fun onDisable() {
        bot.shutdown()
    }
}

