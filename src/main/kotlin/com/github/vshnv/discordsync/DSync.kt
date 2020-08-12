package com.github.vshnv.discordsync

import com.github.vshnv.discordsync.command.registerAllCommands
import org.bukkit.plugin.java.JavaPlugin

class DSync: JavaPlugin() {
    internal lateinit var bot: Bot
        private set
    
    override fun onEnable() {
        saveDefaultConfig()
        registerAllCommands()
        bot = Bot(loadToken(this))
        loadFormats(this)
        reloadReceiversFromConfig(this)
        this.server.pluginManager.registerEvents(EventListener(), this)
    }

    override fun onDisable() {
        bot.shutdown()
    }
}

