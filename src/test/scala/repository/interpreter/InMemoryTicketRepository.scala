package repository.interpreter

import cats.data.NonEmptyList.one
import cats.data.{EitherNel, EitherT}
import cats.syntax.either._
import model.Errors.{Error, EntityMissingError}
import model.{AsyncErrorOr, Ticket}
import repository.TicketRepository

import scala.collection.mutable.{Map => MMap}
import scala.concurrent.Future



case class InMemoryTicketRepository() extends TicketRepository {
  lazy val internal: MMap[String, Ticket] = MMap.empty[String, Ticket]


  def findAll(): AsyncErrorOr[Seq[Ticket]] = EitherT {
    Future.successful(internal.values.toSeq.asRight)
  }

  def query(no: String): AsyncErrorOr[Option[Ticket]] =
    EitherT { Future.successful(internal.get(no).asRight) }


  def store(t: Ticket): AsyncErrorOr[Ticket] = {
    internal += (t.no -> t)
    EitherT(Future.successful(t.asRight))
  }

  def update(no: String)(operation: (Ticket) => EitherNel[Error,Ticket]): AsyncErrorOr[Ticket] = EitherT {
    Future.successful {
      Either.fromOption(internal.get(no), one(EntityMissingError))
        .flatMap(operation)
        .flatMap(t => {
          internal += (t.no -> t)
          t.asRight
        })
    }
  }
}
