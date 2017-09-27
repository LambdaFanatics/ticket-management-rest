package repository.interpreter

import model.Ticket
import repository.TicketRepository
import scala.collection.mutable.{Map => MMap}

import cats.syntax.either._

case class InMemoryTicketRepository() extends TicketRepository {
  lazy val internal: MMap[String, Ticket] = MMap.empty[String, Ticket]

  override def query(no: String): Either[String, Option[Ticket]] =
    internal.get(no).asRight[String]

  override def store(t: Ticket): Either[String, Ticket] = {
    internal += (t.no -> t)
    t.asRight[String]
  }
}
