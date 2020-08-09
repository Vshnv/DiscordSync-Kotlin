package com.github.vshnv.discordsync

abstract class Message(val user: String, val msg: String)

abstract class Messenger<in T: MessageReceiver<V>, in V: Message> {
    private val receivers = mutableListOf<T>()

    /**
     * Forwards passed message to all listening receivers
     * @param msg Message to be sent
     */
    fun sendMessage(msg: V) {
        receivers.forEach {
            it.onReceive(msg)
        }
    }

    /**
     * Adds specified receiver as a listener
     * @param receiver receiver to register
     */
    fun registerReceiver(receiver: T) {
        receivers.add(receiver)
    }

    /**
     * Removes specified receiver from listeners
     * @param receiver receiver to unregister
     */
    fun unregisterReceiver(receiver: T) {
        receivers.remove(receiver)
    }

    fun unregisterAllReceivers() {
        receivers.clear()
    }

    /**
     * Check whether the specified receiver is already registered under this messenger
     * @param receiver receiver to check
     */
    fun isRegistered(receiver: T): Boolean {
        return receivers.contains(receiver)
    }


}

abstract class MessageReceiver<in T: Message> {
    abstract fun onReceive(msg: T)
}