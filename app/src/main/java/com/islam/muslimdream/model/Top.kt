package com.islam.muslimdream.model

class Top {
    private var publisher:String = ""
    private var uid: String = ""
    private var image: String = ""
    private var username: String = ""
    constructor()
    constructor(username: String,publisher: String,image: String,uid: String) {
        this.publisher = publisher
        this.uid= uid
    }

    fun getPublisher():String{
        return publisher
    }
    fun getUsername(): String
    {
        return  username
    }
    fun getImage(): String
    {
        return  image
    }
    fun getUID(): String
    {
        return  uid
    }
}