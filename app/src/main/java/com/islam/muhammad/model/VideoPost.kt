package com.islam.muhammad.model

class VideoPost {
    private var postid:String = ""
    private var postimage:String = ""
    private var coverPhoto:String = ""
    private var publisher:String = ""
    private var description:String = ""
    private var postDate:String = ""
    constructor()

    constructor(postid: String, postimage: String,coverPhoto: String, publisher: String, description: String,postDate:String) {
        this.postid = postid
        this.postimage = postimage
        this.coverPhoto = coverPhoto
        this.publisher = publisher
        this.description = description
        this.postDate =postDate
    }
    fun getPostid():String{
        return postid
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
    fun setPostDate(postDate: String){
        this.postDate = postDate
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

}