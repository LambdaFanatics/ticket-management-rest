package service.interpreter


import cats.data.NonEmptyList.one
import cats.data.{EitherT, Kleisli}
import cats.instances.future._
import cats.syntax.either._
import model.Errors._
import model._
import repository.TicketRepository
import service.TicketService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Ticket service implementation (interpreter).
  */
object TicketServiceInterpreter extends TicketService {
  override def findAll(): TicketOperation[Seq[Ticket]] =  Kleisli[AsyncErrorOr, TicketRepository, Seq[Ticket]] {
    (repo: TicketRepository) => repo.findAll().leftMap(error => TicketRetrieveError ::  error)
  }

  def open(no: String, title: String): TicketOperation[Ticket] = Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
    (repo: TicketRepository) =>

        repo.query(no)
          .flatMap {
            case Some(_) => EitherT(Future.successful(one(EntityExistsError).asLeft[Ticket])): AsyncErrorOr[Ticket]
            case None =>
              if(no.isEmpty || title.isEmpty)
                EitherT(Future.successful(one(TicketValidateError).asLeft[Ticket])): AsyncErrorOr[Ticket]
              else repo.store(Ticket(no, title, TicketStatus.Open, Seq()))
          }
          .leftMap(error => TicketOpenError :: error)

  }

  def start(no: String): TicketOperation[Ticket] = Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
    (repo: TicketRepository) =>
          repo.update(no)(t => t.copy(status = TicketStatus.InProgress).asRight)
            .leftMap(error => TicketStartError :: error)
  }

  def changeTitle(no: String, title: String): TicketOperation[Ticket] = Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
    (repo: TicketRepository) =>
          repo.update(no)(t => t.copy(title = title).asRight)
            .leftMap(error => TicketChangeError :: error)
  }

  def close(no: String): TicketOperation[Ticket] = Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
    (repo: TicketRepository) =>
          repo.update(no)(t => t.copy(status = TicketStatus.Closed).asRight)
            .leftMap(error => TicketCloseError :: error)
  }
}
