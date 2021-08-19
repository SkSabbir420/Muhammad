package com.ourdream.muslimdream.model

class Post{
    private var postid:String = ""
    private var postimage:String = ""
    private var publisher:String = ""
    private var description:String = ""
    private var postDate:String = ""

    constructor()
    constructor(postid: String, postimage: String, publisher: String, description: String,postDate:String) {
        this.postid = postid
        this.postimage = postimage
        this.publisher = publisher
        this.description = description
        this.postDate =postDate
    }

    fun setPostid(postid:String){
        this.postid =postid
    }
    fun setPostimage(postimage:String){
        this.postimage =postimage
    }
    fun setPublisher(publisher:String){
        this.publisher =publisher
    }
    fun setDescription(description:String){
        this.description =description
    }
    fun setPostDate(postDate: String){
        this.postDate = postDate
    }


    fun getPostid():String{
        return postid
    }
    fun getPostDate():String{
        return postDate
    }
    fun getPostimage():String{
        return postimage
    }
    fun getPublisher():String{
        return publisher
    }
    fun getDescription():String{
        return description
    }

}