package com.husainazkas.chatapplication

class MessageData(
    var id : String,
    var fromId : String,
    var text : String,
    var toId : String,
    var time : Long) {
    constructor() : this("","","","",-1)
}