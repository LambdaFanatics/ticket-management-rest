package repository.interpreter

import model.Ticket
import repository.TicketRepository

import cats.syntax.either._

object FailingTicketRepository extends TicketRepository {

  override def query(no: String): Either[String, Option[Ticket]] = "Storage unavailable".asLeft[Option[Ticket]]

  override def store(t: Ticket): Either[String, Ticket] = "Storage unavailable".asLeft[Ticket]
}
