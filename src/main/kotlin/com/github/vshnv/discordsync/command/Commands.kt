package com.github.vshnv.discordsync.command

import com.github.vshnv.discordsync.DSync
import com.github.vshnv.discordsync.Format
import com.github.vshnv.discordsync.loadFormats
import com.github.vshnv.discordsync.messaging.DiscordMessage
import com.github.vshnv.discordsync.messaging.DiscordMessenger
import com.github.vshnv.discordsync.messaging.MCMessage
import com.github.vshnv.discordsync.messaging.MCMessenger
import com.github.vshnv.discordsync.reloadReceiversFromConfig
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin


class ReloadCommand: CommandBase("dsync", "discordsync.reload") {
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        if (args.isEmpty()) return true
        if (!args[0].equals("reload", true)) return true
        reloadReceiversFromConfig(JavaPlugin.getPlugin(DSync::class.java))
        loadFormats(JavaPlugin.getPlugin(DSync::class.java))
        sender.sendMessage("Reloaded clients and formats from config! Please wait a few seconds...!")
        return true
    }
}

class DCastCommand: CommandBase("dcast", "discordsync.dcast") {
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        DiscordMessenger.sendMessage(DiscordMessage("Broadcast", args.joinToString ( " " )))
        MCMessenger.sendMessage(MCMessage("Broadcast",args.joinToString { " " }, Format.D_CAST))
        sender.sendMessage("Cast successful!")
        return true
    }
}

