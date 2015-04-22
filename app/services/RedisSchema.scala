package services


/**
* This object represents redis keys
* TODO change package maybe -> Redis or models/entities package?
**/
object RedisSchema {

    //To retrieve user
    def user = s"user"
    def keyUser(uid: String) = s"user:uid:$uid"

    //To easily retrieve a username by id 
    def keyUsernameToId(username: String) = s"username:$username:uid"


    //To easily retrieve a authentication token from uid and uid from authTok
    def keyAuthToUser(token: String): String = s"token:$token:uid"

    // To retrieve a set of followers uid
    def keyFollower(uid: String) = s"user:$uid:followers"

    // sTo retrieve a set of following uid
    def keyFollowing(uid: String) = s"user:$uid:following"

    //Hometimeline: followed and user's posts
    def keyHomeTimeline(uid: String): String = s"user:$uid:posts"
    def keyPosts : String = "posts"
    def keyPosts(pid : String) = s"post:$pid"
    def keyPostsUID(uid : String) = s"posts:$uid"


    //list  global posts
    val globalTimeline = "globalTimeline"
}
