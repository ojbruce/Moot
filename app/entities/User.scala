package entities

/**
 * Classe compagnion de l'utilisateur
 * Pour jsonifier
 */
object User {
  implicit val format = ""
}


/**
 * Represents a user.
 * @param uid User      identifier
 * @param username      the user username
 * @param isFollowing   true if he is following someone
 */
case class User(uid: String, username: String, isFollowing: Boolean) 