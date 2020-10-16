package com.peachgenz.machatgun.model

class Message() {
    var sender: String? = null
    var message: String? = null

    constructor(sender:String,message: String): this(){
        this.sender=sender
        this.message=message
    }
}