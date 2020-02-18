package com.duke.diskoverusosyal.model

data class Message(
    var myid: String? = null,
    var toid: String? = null,
    var message: String? = null,
    var type: String? = null,
    var message_type: String? = null,
    var dated: String? = null,
    var seen: String? = null,
    var connection_type: String? = null
)