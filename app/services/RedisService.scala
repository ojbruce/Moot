package services

import scredis._

import scala.concurrent.future
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


import play.Logger

/**
 * This is the redis service that will make the calls to redis database
 * 
 * TODO : Enhance with assertion/verification presence or non presence of elements
 *
 */
object RedisService{

    // Creates a Redis instance with default configuration.
    // See reference.conf for the complete list of configurable parameters.
    // Using default configuration but may change (heroku)
    val redis = Redis()

    //RIP Redis - do not use if possible
    def flushall = redis.flushAll()
    

    /******************* Action on users ********************/

    /**
    * Method to register a user 
    * Register username and password to redis
    * Will create a hmSet uid uname pasword token
    * @param username username to register
    * @param password password to register
    * @return a new uid
    */
    def signUpUser(username: String, password: String): Future[(String,String)] = {
        
        //TODO : verify if unique username, 
        //TODO : hash password strength
                    //_   <- Logger.info(s"[RedisService] User : $uid $username created !")

        // A new uuid with meteor luck 
        val uid = java.util.UUID.randomUUID.toString

        for {
            token <- RedisService.genToken(uid) 
            _     <- redis.hSet(RedisSchema.user, username, uid) //username -> uid
            _     <- redis.hmSet(RedisSchema.keyUser(uid), Map("uid"->uid, "username" -> username, "password" -> password, "token" -> token))

        } yield (token,uid)
    }

  /**
   * Attributs a token to a user
   * Registers the token to Redis
   */
    def genToken(uid: String): Future[String] = {
        val token = java.util.UUID.randomUUID.toString
        for {
            //TODO change token to nicer           
            _     <- redis.set(RedisSchema.keyAuthToUser(token), uid)
            //_     <- Logger.info(s"[RedisService] User : $uid created !")
        } yield token
    }

    /**
     * Get's a user username
     * @param uid user uid
     * @return a future username
     */
    def getUserName(uid : String) : Future[List[Option[String]]]={

        for{
            res <- redis.hmGet(RedisSchema.keyUser(uid),"username")
        }yield res

    }

    /**
     * Get's a user uid
     * @param uid user uid
     * @return a future username
     */
    def getUserUid(username : String) : Future[String]={
        
        for{
            res <- redis.hGet(RedisSchema.user,username)
        }yield res.getOrElse("test")

    }

    /**
    * Method to login a user
    * TODO
    * @param username the user's username 
    * @param password the user's password
    * @return (Future of) the UID 
    */
    def loginUser(username: String, password: String):Unit = {
        //TODO
        //Match username with uid
        //Match password with uid
        //Return if okay
        var retrievedUid = redis.get(RedisSchema.keyUsernameToId(username))
        //var retrievedPassword = redis.hmGet(RedisSchema.keyUser(retrievedUid),"password")

        Logger.info(s"[RedisService] User logged $retrievedUid !")
    }

    /**
     * To make user A current follow user B
     * Adds a userId to a sorted Set
     * @param uid user id
     * @param Following user being followed
     * @return Future Unit
     */
    def follow(uid: String, following: String) :Unit = {

        //Can't follow himself
        if(uid != following){
             //zAdd[W](key: String, member: W, score: Score)(implicit arg0: Writer[W]): Future[Boolean]
            redis.zAdd(RedisSchema.keyFollowing(uid), following,(System.currentTimeMillis / 1000))
            redis.zAdd(RedisSchema.keyFollower(following), uid,(System.currentTimeMillis / 1000))
            println(s"User A $uid follow User B $following")
        }else{
            println(s"One should not follow ego")    
        }
        
    }

  /**
   * To unfollow a user
   * @param uid user who wants to unfollow
   * @param following the previously followed id
   * @return (Future of) Unit
   */
    def unfollow(uid: String, following: String): Unit = {

       if(uid != following){
            //zRem(key,member)
            redis.zRem(RedisSchema.keyFollowing(uid), following)
            redis.zRem(RedisSchema.keyFollower(following), uid)
            println(s"User A $uid unfollow User B $following")
        }else{
            println(s"One should not unfollow himself")    
        }
    }

    /**
     * Get a set of users 
     * Users that have followed the culprit
     */
    def uFollowers(uid: String): Future[Set[String]] = {
        //zRangeByScore[R](key: String, min: ScoreLimit, max: ScoreLimit, limitOpt: Option[(Long, Int)] 
        for {
            //Recuperation des membres
            followers <- redis.zRange(RedisSchema.keyFollower(uid))
        } yield followers

    }

    /***** Action on posts ******/





  /**
   * Push a post to REdis
   * @param pid post id of message being saved
   * @param msg message to save
   * @return (Future of) Unit
   
    def savePost(pid: String, date: Long, msg: String, uid:String): Future[Unit] = {
        
        redis.hmSet(RedisSchema.keyPosts(pid), Map( "pid" -> pid, "date" -> date,"author" -> uid, "body" -> msg))
        
    }*/

  /**
   * Method that will posts each into the user and his followers feed
   * @param author of the post
   * @param pid id of the post
   * @return Unit
   */
    private def distributPost(author: String, pid: String): Future[Unit] ={
      for {
        folls <- uFollowers(author)

        postman = 
            for (toDistribute <- folls + author) yield {

                for {
                    _ <- redis.lPush(RedisSchema.keyPostsUID(toDistribute), pid)
                } yield ()
            }
        _ <- Future.sequence(postman)
      } yield ()
    }

  /**
   * Post a message by user .
   * @param author author of the post
   * @param body body of the post
   * @return id of the post after creation
   */
    def postMessage(author: String, body: String): Future[String] = {

      val date = System.currentTimeMillis
      val postId = java.util.UUID.randomUUID.toString

      for {
        _ <-redis.lPush(RedisSchema.globalTimeline, postId)
        //_ <- savePost(postId, date, body, author)
        _ <-distributPost(author, postId)
        _ <- redis.lTrim(RedisSchema.globalTimeline,0,1000)
      } yield postId
    }


}