package repository.interpreter.db

import slick.jdbc.H2Profile.api._

import model.{Ticket, TicketStatus}

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

