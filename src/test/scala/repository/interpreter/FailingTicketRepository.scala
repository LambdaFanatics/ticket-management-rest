package repository.interpreter

import cats.data.NonEmptyList.one
import cats.data.{EitherNel, EitherT}
import cats.instances.future._
import model.Errors.{GenericError, StorageCreateError, StorageRetrieveError, StorageUpdateError}
import model.{AsyncErrorOr, Errors, Ticket}
import repository.TicketRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FailingTicketRepository extends TicketRepository {

  def findAll(): AsyncErrorOr[Seq[Ticket]] = EitherT.left[Seq[Ticket]](Future.successful(one(StorageRetrieveError)))

  def query(no: String): AsyncErrorOr[Option[Ticket]] =
    EitherT.left[Option[Ticket]](Future.successful(one(StorageRetrieveError)))

  def store(t: Ticket): AsyncErrorOr[Ticket] =
    EitherT.left[Ticket](Future.successful(one(StorageCreateError)))

  def update(no: String)(operation: (Ticket) => EitherNel[Errors.Error, Ticket]): AsyncErrorOr[Ticket] =
    EitherT.left[Ticket](Future.successful(one(StorageUpdateError)))
}
