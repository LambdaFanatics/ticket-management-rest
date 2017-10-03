package repository.interpreter.db

import cats.data.NonEmptyList.one
import cats.data.{EitherNel, EitherT}
import model.Ticket
import repository.TicketRepository
import cats.syntax.either._
import model.Errors.{EntityMissingError, Error, StorageCreateError, StorageRetrieveError, StorageUpdateError}

import scala.concurrent.ExecutionContext.Implicits.global

case class DbTicketRepository(dl: DatabaseLayer) extends TicketRepository {

  import dl._
  import profile.api._

  private def findAllAction(): DBIO[EitherNel[Error,Seq[Ticket]]] =
    tickets.result.map(_.asRight)

  private def findAction(no: String): DBIO[EitherNel[Error,Option[Ticket]]] =
    tickets.filter(_.no === no).result.map(_.headOption.asRight)

  private def storeAction(value: Ticket): DBIO[EitherNel[Error,Ticket]] =
    (tickets += value).map(_ => value.asRight)

  private def updateAction(no: String)(f: Ticket => EitherNel[Error,Ticket]): DBIO[EitherNel[Error,Ticket]] = {
    //FIXME use forceInsert
    val updated = for {
      existing <- tickets.filter(_.no === no).result.headOption
      either = Either.fromOption(existing, one(EntityMissingError)).flatMap(t => f(t))
    } yield either

    updated.flatMap {
      case Right(t) => tickets.insertOrUpdate(t).map(_ => t.asRight)
      case _ => updated
    }
  }

  def findAll() = EitherT {
    db.run(findAllAction())
        .recover {case ex: Exception => one(StorageRetrieveError).asLeft}
  }

  def query(no: String) = EitherT {
    db.run(findAction(no))
      .recover {case ex: Exception => one(StorageRetrieveError).asLeft}
  }

  def store(ticket: Ticket) = EitherT{
    db.run(storeAction(ticket))
      .recover { case ex: Exception => one(StorageCreateError).asLeft}
  }

  def update(no: String)(f: Ticket => EitherNel[Error, Ticket]) = EitherT {
    db.run(updateAction(no)(f).transactionally)
        .recover {case ex: Exception => one(StorageUpdateError).asLeft}
  }
}


