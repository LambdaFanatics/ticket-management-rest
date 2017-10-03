package repository.interpreter

import cats.data.NonEmptyList.one
import cats.data.{EitherNel, EitherT, NonEmptyList}
import cats.syntax.either._
import model.{AsyncErrorOr, Ticket}
import repository.TicketRepository

import scala.collection.mutable.{Map => MMap}
import scala.concurrent.Future



case class InMemoryTicketRepository() extends TicketRepository {
  lazy val internal: MMap[String, Ticket] = MMap.empty[String, Ticket]

  def query(no: String): AsyncErrorOr[Option[Ticket]] =
    EitherT { Future.successful(internal.get(no).asRight[NonEmptyList[String]]) }


  def store(t: Ticket): AsyncErrorOr[Ticket] = {
    internal += (t.no -> t)
    EitherT(Future.successful(t.asRight[NonEmptyList[String]]))
  }

  def update(no: String)(operation: (Ticket) => EitherNel[String,Ticket]): AsyncErrorOr[Ticket] = EitherT {
    Future.successful {
      Either.fromOption(internal.get(no), one(s"Ticket ($no) does not exist"))
        .flatMap(operation)
        .flatMap(t => {
          internal += (t.no -> t)
          t.asRight[NonEmptyList[String]]
        })
    }
  }
}
