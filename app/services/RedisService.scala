package services

import scredis._

import scala.concurrent.future
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


import play.Logger


object RedisService{

    // Creates a Redis instance with default configuration.
    // See reference.conf for the complete list of configurable parameters.
    // Using default configuration but may change (heroku)
    val redis = Redis()

    def flushall = redis.flushAll()
    

    /*******Action on users ******/

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
            _     <- redis.hmSet(RedisSchema.keyUser(uid), Map("username" -> username, "password" -> password, "token" -> token))

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
    * @param username the user's username 
    * @param password the user's password
    * @return (Future of) the UID 
    */
    def loginUser(username: String, password: String):Unit = {
        
        //Match username with uid
        //Match password with uid

        var retrievedUid = redis.get(RedisSchema.keyUsernameToId(username))
        //var retrievedPassword = redis.hmGet(RedisSchema.keyUser(retrievedUid),"password")


        Logger.info(s"[RedisService] User logged $retrievedUid !")
    }

}