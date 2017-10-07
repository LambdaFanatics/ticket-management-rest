package com.lamdafanatics.tickets.repository.db

import cats.data.NonEmptyList.one
import cats.data.{EitherNel, EitherT}
import com.lamdafanatics.tickets.domain.ticket.Ticket
import com.lamdafanatics.tickets.repository.TicketRepository
import cats.syntax.either._
import slick.dbio.DBIO

import scala.concurrent.ExecutionContext.Implicits.global

case class DbTicketRepository(dl: DatabaseLayer) extends TicketRepository {

  import dl._
  import profile.api._

  private def findAction(no: String): DBIO[EitherNel[String,Option[Ticket]]] =
    tickets.filter(_.no === no).result.map(_.headOption.asRight)

  private def storeAction(value: Ticket): DBIO[EitherNel[String,Ticket]] =
    (tickets += value).map(_ => value.asRight)

  private def updateAction(no: String)(f: Ticket => EitherNel[String,Ticket]): DBIO[EitherNel[String,Ticket]] = {

    //FIXME use forceInsert
    val updated = for {
      existing <- tickets.filter(_.no === no).result.headOption
      either = Either.fromOption(existing, one(s"Database entry does not exist")).flatMap(t => f(t))
    } yield either

    updated.flatMap {
      case Right(t) => tickets.insertOrUpdate(t).map(_ => t.asRight)
      case _ => updated
    }
  }


  def query(no: String) = EitherT {
    db.run(findAction(no))
      .recover {case ex: Exception => one(s"Database query error ${ex.getMessage}").asLeft}
  }

  def store(ticket: Ticket) = EitherT{
    db.run(storeAction(ticket))
      .recover { case ex: Exception => one(s"Database store error ${ex.getMessage}").asLeft}
  }

  def update(no: String)(f: Ticket => EitherNel[String, Ticket]) = EitherT {
    db.run(updateAction(no)(f).transactionally)
      .recover {case ex: Exception => one(s"Database update error ${ex.getMessage}").asLeft}
  }
}