package repository.interpreter.db

import cats.data.NonEmptyList
import model.Ticket
import repository.TicketRepository

class DbTicketRepository() extends TicketRepository {

  def query(key: String) = ???

  def store(value: Ticket) = ???

  def update(key: String)(operation: (Ticket) => Either[NonEmptyList[String], Ticket]) = ???
}


