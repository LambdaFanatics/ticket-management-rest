package repository.interpreter

import model.TicketStatus
import slick.jdbc.JdbcType
import slick.lifted.TableQuery
import slick.ast.BaseTypedType

import slick.jdbc.H2Profile.api._

package object db {

  implicit val ticketStatusColumn: JdbcType[TicketStatus] with BaseTypedType[TicketStatus] = MappedColumnType.base[TicketStatus, String](_.toString, {
    case "Open" => TicketStatus.Open
    case "InProgress" => TicketStatus.InProgress
    case _ => TicketStatus.Closed
  })

  lazy val tickets = TableQuery[TicketTable]
}


