package repository.interpreter

import cats.syntax.either._
import model.Ticket
import repository.TicketRepository

import scala.collection.mutable.{Map => MMap}

case class InMemoryTicketRepository() extends TicketRepository {
  lazy val internal: MMap[String, Ticket] = MMap.empty[String, Ticket]

  def query(no: String): Either[String, Option[Ticket]] =
    internal.get(no).asRight[String]

  def store(t: Ticket): Either[String, Ticket] = {
    internal += (t.no -> t)
    t.asRight[String]
  }

  def update(no: String)(operation: (Ticket) => Either[String, Ticket]) = {
    Either.fromOption(internal.get(no), s"Ticket ($no) does not exist")
      .flatMap(operation)
      .flatMap(t => {
        internal += (t.no -> t)
        t.asRight[String]
      })
  }
}
