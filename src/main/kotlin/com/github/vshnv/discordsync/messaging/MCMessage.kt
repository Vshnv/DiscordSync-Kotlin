package com.github.vshnv.discordsync.messaging

import com.github.vshnv.discordsync.*
import org.bukkit.Bukkit

/* Representation of a message to be sent to MC */
class MCMessage(username: String, msg: String,val format: Format): Message(username, msg)



/* OUT */
/* Discord -> MC messages */
object MCMessenger: Messenger<MCReceiver, MCMessage>()


/* IN */
/* Receives messages to be sent to MC */
class MCReceiver: MessageReceiver<MCMessage>() {
    override fun onReceive(msg: MCMessage) {
        when (msg.format) {
            Format.CHAT -> Bukkit.broadcastMessage(format(msg.format, "User" to msg.user, "Message" to msg.msg))
            Format.D_CAST -> Bukkit.broadcastMessage(format(msg.format, "Message" to msg.msg))
            else -> Unit
        }

    }
}