package repository.interpreter

import cats.data.NonEmptyList.one
import cats.data.{EitherNel, EitherT}
import cats.instances.future._
import model.{AsyncErrorOr, Ticket}
import repository.TicketRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FailingTicketRepository extends TicketRepository {

  def findAll() = EitherT.left[Seq[Ticket]](Future.successful(one("Storage unavailable")))

  override def query(no: String): AsyncErrorOr[Option[Ticket]] =
    EitherT.left[Option[Ticket]](Future.successful(one("Storage unavailable")))
    //OR more explicitly
    //    EitherT {
    //      Future.successful(one("Storage unavailable").asLeft[Option[Ticket]])
    //    }

  override def store(t: Ticket): AsyncErrorOr[Ticket] =
    EitherT.left[Ticket](Future.successful(one("Storage unavailable")))

  override def update(no: String)(operation: (Ticket) => EitherNel[String, Ticket]): AsyncErrorOr[Ticket] =
    EitherT.left[Ticket](Future.successful(one("Storage unavailable")))
}
