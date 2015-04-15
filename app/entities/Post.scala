package entities

/**
 * Classe compagnion du post
 * Pour jsonifier
 */
object Post {

  implicit val format = ""

}

/**
  * Represents a message
  *
  * @param postid       Message identifier
  * @param timestamp    time of creation
  * @param msg          the body of the message
  */
case class Post(postId: String, timestamp: Long, UserId: String, msg: String) 