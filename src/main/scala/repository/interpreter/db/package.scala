package repository.interpreter

import model.TicketStatus
import slick.jdbc.{JdbcProfile, JdbcType}
import slick.ast.BaseTypedType
import slick.jdbc.H2Profile.api._

package object db {

  trait Profile {
    val profile: slick.jdbc.JdbcProfile
  }

  implicit val ticketStatusColumn: JdbcType[TicketStatus] with BaseTypedType[TicketStatus] = MappedColumnType.base[TicketStatus, String](_.toString, {
    case "Open" => TicketStatus.Open
    case "InProgress" => TicketStatus.InProgress
    case _ => TicketStatus.Closed
  })

  case class DatabaseLayer(profile: JdbcProfile) extends Profile with Tables
}


