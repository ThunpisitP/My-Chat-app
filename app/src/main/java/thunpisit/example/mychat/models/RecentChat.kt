package thunpisit.example.mychat.models

class RecentChat() {
    var date : String?=null
    var lastMessage:String?=null
    constructor(date:String,lastMessage:String):this(){
        this.date = date
        this.lastMessage = lastMessage
    }
}