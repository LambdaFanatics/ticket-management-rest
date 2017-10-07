package com.lamdafanatics.tickets.service

import cats.data.NonEmptyList.one
import cats.data.{EitherT, Kleisli}
import cats.instances.future._
import cats.syntax.either._
import com.lamdafanatics.tickets.domain.domain.AsyncErrorOr
import com.lamdafanatics.tickets.domain.ticket.Ticket
import com.lamdafanatics.tickets.repository.TicketRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// FIXME model errors correctly
/**
  * Ticket service implementation (interpreter).
  */
object TicketServiceImpl extends TicketService {
  
  def open(no: String, title: String): TicketOperation[Ticket] =
    Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
      (repo: TicketRepository) =>
        repo.query(no)
          .flatMap {
            case Some(_) => EitherT(
              Future.successful(one(s"Ticket ($no) already exists").asLeft[Ticket])
            ): AsyncErrorOr[Ticket]
            case None =>
              if (no.isEmpty || title.isEmpty)
                EitherT(
                  Future.successful(one(s"Ticket open no and title required").asLeft[Ticket])
                ): AsyncErrorOr[Ticket]
              else repo.store(Ticket(no, title, "OPEN"))
            //            else repo.store(Ticket(no, title, TicketStatus.Open, Seq()))
          }
          .leftMap(error => s"Failed to open ticket ($no)" :: error)

    }

  def start(no: String): TicketOperation[Ticket] = Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
    (repo: TicketRepository) =>
      repo.update(no)(t => t.copy(status = "IN_PROGRESS").asRight)
        .leftMap(error => s"Failed to start ticket ($no)" :: error)
  }

  def changeTitle(no: String, title: String): TicketOperation[Ticket] =
    Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
      (repo: TicketRepository) =>
        repo.update(no)(t => t.copy(title = title).asRight)
          .leftMap(error => s"Failed to change title of ticket ($no)" :: error)
    }

  def close(no: String): TicketOperation[Ticket] =
    Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
      (repo: TicketRepository) =>
        repo.update(no)(t => t.copy(status = "CLOSED").asRight)
          .leftMap(error => s"Failed to close ticket ($no)" :: error)
    }
}