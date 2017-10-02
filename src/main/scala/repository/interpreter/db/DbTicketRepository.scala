package repository.interpreter.db

import model.Ticket
import repository.TicketRepository
import slick.jdbc.JdbcBackend

class DbTicketRepository(db: JdbcBackend) extends TicketRepository{

  def query(key: String)  = ???

  def store(value: Ticket) = ???

  def update(key: String)(operation: (Ticket) => Either[String, Ticket]) = ???
}


