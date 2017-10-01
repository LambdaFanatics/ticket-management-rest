/** Expreriments of using slick */

import model.{Comment, Ticket, TicketStatus}
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType


Ticket("100", "Ticket 1", TicketStatus.Open, Seq(Comment("This is my first task"), Comment("And I can comment on it")))

import slick.jdbc.H2Profile.api._


val db = Database.forURL("jdbc:h2:mem:ticketdb;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")


implicit val ticketStatusColumn: JdbcType[TicketStatus] with BaseTypedType[TicketStatus] = MappedColumnType.base[TicketStatus, String](_.toString, {
  case "Open" => TicketStatus.Open
  case "InProgress" => TicketStatus.InProgress
  case _ => TicketStatus.Closed
})


final class TicketTable(tag: Tag) extends Table[Ticket](tag, "ticket") {

  def no = column[String]("no", O.PrimaryKey)

  def title = column[String]("title")

  def status = column[TicketStatus]("status")

  //Mapping ticket row tuple to Ticket model class
  //Ignoring commends for now
  type RowTuple = (String, String, TicketStatus)

  private def constructTicket: RowTuple => Ticket = {
    case (no, title, status) => Ticket(no, title, status, Seq())
  }

  private def extractTicket: PartialFunction[Ticket, RowTuple] = {
    case Ticket(no, title, status, _) => (no, title, status)
  }

  def * = (no, title, status) <> (constructTicket, extractTicket.lift)
}

lazy val tickets = TableQuery[TicketTable]