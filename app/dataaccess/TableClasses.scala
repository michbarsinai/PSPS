package dataaccess

import java.sql.Timestamp
import models.{Invitation, PasswordResetRequest, User, UserRole}
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._


class UsersTable(tag:Tag) extends Table[User](tag,"users") {
  
  implicit val roleMapper = MappedColumnType.base[Set[UserRole.Value], String](
    s => s.mkString(","),
    s => s.split(",").filter(_.nonEmpty).map(UserRole.withName).toSet
  )
  
  def id                = column[Long]("id",O.PrimaryKey, O.AutoInc)
  def username          = column[String]("username")
  def name              = column[String]("name")
  def email             = column[String]("email")
  def encryptedPassword = column[String]("encrypted_password")
  def roles             = column[Set[UserRole.Value]]("roles")
  
  def * = (id, username, name, email, encryptedPassword, roles) <> (User.tupled, User.unapply)

}

class InvitationsTable(tag:Tag) extends Table[Invitation](tag, "invitations") {
  def email = column[String]("email", O.PrimaryKey)
  def date  = column[Timestamp]("date")
  def uuid  = column[String]("uuid")
  def sender  = column[String]("sender",O.PrimaryKey)

//  def pk = primaryKey("invitation_pkey", (email, sender))

  def * = (email, date, uuid, sender) <> (Invitation.tupled, Invitation.unapply)
}
class PasswordResetRequestsTable(tag:Tag) extends Table[PasswordResetRequest](tag, "password_reset_requests"){
  def username = column[String]("username")
  def uuid     = column[String]("uuid")
  def reset_password_date = column[Timestamp]("reset_password_date")

  def pk = primaryKey("uuid_for_forgot_password_pkey", (username, uuid))

  def * = (username, uuid, reset_password_date) <> (PasswordResetRequest.tupled, PasswordResetRequest.unapply)
}
