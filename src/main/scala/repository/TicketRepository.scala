package repository

import model.Ticket

trait TicketRepository {
  def query(no: String): Either[String, Option[Ticket]]
  def store(t: Ticket): Either[String, Ticket]
}
