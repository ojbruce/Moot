package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.SimpleResult

import scala.concurrent.Future


import play.api.libs.concurrent.Execution.Implicits.defaultContext

import services.RedisService

object RegistrationPage extends Controller{


    /**** GET Request *****/
    /**
    * Renders the registerpage before login
    */
    def register = Action {
        Ok( views.html.register(None) )
    }

    /**
    * Renders the registerpage after logout
    */
    def logout = Action {
        Ok( views.html.register(None) )
    }



    /**** POST Request ***/

    /**
    * Allows a new user to sign up
    */
    def signup() = Action.async{
       
       // Retrieving the request
        implicit request =>
            val username =(request.queryString.get("username").flatMap(_.headOption)).getOrElse("toto")
            val password =(request.queryString.get("password").flatMap(_.headOption)).getOrElse("password1")

            val name = request.queryString.get("username").flatMap(_.headOption)

            for {
                /*formInfo <- request.body.asFormUrlEncoded
                usernames <- formInfo.get("username")
                username <- usernames.headOption
                passwords <- formInfo.get("password")
                password <- passwords.headOption*/
            
                (uid,token)   <- RedisService.signUpUser(username, password)

            } yield {
                Ok(views.html.home(uid,username)).withSession("login" -> token + "username" -> username)
            }

    }

    /**
    * Allows a new user to sign in
    */
    def login = Action{
        //get usename/password from request
        //verify if user exists
        //generate an authentication token
        //Redirect to logged page
        Redirect(routes.Application.index).withSession( "login" -> "tototoken")

    }
}