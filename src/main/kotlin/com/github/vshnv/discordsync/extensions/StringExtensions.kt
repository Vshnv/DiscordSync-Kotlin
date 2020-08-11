package com.github.vshnv.discordsync.extensions

import org.bukkit.ChatColor

fun String.colorize(c: Char = '&'): String {
    return ChatColor.translateAlternateColorCodes(c, this)
}