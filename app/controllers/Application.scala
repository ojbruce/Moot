package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.concurrent._
import play.api.libs.iteratee._
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
}