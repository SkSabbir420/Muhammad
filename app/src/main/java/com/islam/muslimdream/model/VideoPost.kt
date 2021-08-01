package com.islam.muslimdream.model

class VideoPost {
    private var postid:String = ""
    private var postimage:String = ""
    private var coverPhoto:String = ""
    private var publisher:String = ""
    private var description:String = ""
    private var postDate:String = ""
    private var postTime:String = ""
    private var postCategory:String = ""
    constructor()

    constructor(postid: String, postimage: String,coverPhoto: String, publisher: String, description: String,postDate:String,postTime:String,postCategory:String) {
        this.postid = postid
        this.postimage = postimage
        this.coverPhoto = coverPhoto
        this.publisher = publisher
        this.description = description
        this.postDate =postDate
        this.postTime =postTime
        this.postCategory = postCategory
    }
    fun getPostid():String{
        return postid
    }
    fun getPostCategory():String{
        return postCategory
    }

    fun getPostimage():String{
        return postimage
    }
    fun getCoverPhoto():String{
        return coverPhoto
    }
    fun getPublisher():String{
        return publisher
    }
    fun getDescription():String{
        return description
    }
    fun getPostDate():String{
        return postDate
    }
    fun getPostTime():String{
        return postTime
    }

    fun setPostTime(postTime: String){
        this.postTime = postTime
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
    fun setCoverPhoto(coverPhoto: String){
        this.coverPhoto = coverPhoto
    }
    fun setPostCategory(postCategory: String){
        this.postCategory = postCategory
    }

}