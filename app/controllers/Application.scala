package controllers

import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.concurrent._
import scala.util.{Success, Failure}

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.Routes

import scala.concurrent.Future

import services._
import entities._

object Application extends Controller {

    /**
     * Renders the index to authenticated users
     *
     */
    def index = Action {
        Ok(views.html.index())
    }

    /**
     * Renders the index to authenticated users
     *
     */
    def home = Action {
        Ok(views.html.home(" ", " "))
    }

    /**
     * Verify if one can login
     * And redirects
     */
    def login(username: String, password: String) = Action.async{
        //Future onComplete returns unit need to map

        RedisService.loginUser(username,password).map(i => 
            i match {
                case false => Unauthorized("erreur")
                case true => Ok(views.html.home(username, password))
            }
            
        )

        
    }

    //GET /user/timeline/:uid                 
    def getUserTimeline(uid: String)=TODO

    //GET /user/followers:uid                 
    def getFollower(uid: String)=TODO
    //GET /user/following:uid                 
    def getFollowing(uid: String)=TODO

    def follow(uid: String, other: String)=TODO 


    def unfollow(uid: String, other: String)=TODO 

    def moot(body: String,timestamp : Long)=TODO
}