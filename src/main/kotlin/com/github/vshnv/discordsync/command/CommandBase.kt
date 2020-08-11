package com.github.vshnv.discordsync.command

import org.bukkit.Bukkit
import org.bukkit.ChatColor

import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.reflections.Reflections


abstract class CommandBase(name: String, private val permission: String = ""): CommandExecutor {
    init {
        Bukkit.getPluginCommand(name)?.setExecutor(this)
    }




    abstract fun execute(sender: CommandSender,  args: Array<out String>): Boolean


     override fun onCommand(
         sender: CommandSender,
         command: org.bukkit.command.Command,
         label: String,
         args: Array<out String>
     ): Boolean {
         if (permission.isNotEmpty() && !sender.hasPermission(permission)) {
             sender.sendMessage("${ChatColor.RED}You do not have permission to execute this command!")
             return true
         }
         return execute(sender, args)

     }

}




internal fun registerAllCommands(){
    val reflections = Reflections(CommandBase::class.java.`package`.name)
    reflections.getSubTypesOf(CommandBase::class.java).forEach { it.newInstance() }
}