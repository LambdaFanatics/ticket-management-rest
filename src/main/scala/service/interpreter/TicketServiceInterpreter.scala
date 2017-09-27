package service.interpreter


import cats.data.NonEmptyList.one
import cats.data.{EitherT, Kleisli, NonEmptyList}
import cats.syntax.either._
import model._
import repository.TicketRepository
import service.TicketService

import scala.concurrent.Future

// FIXME model errors correctly
/**
  * Ticket service implementation (interpreter).
  */
object TicketServiceInterpreter extends TicketService {


  def open(no: String, title: String): TicketOperation[Ticket] = Kleisli[AsyncErrorOr, TicketRepository, Ticket]  {
    (repo: TicketRepository) =>
      EitherT {
        Future.successful {

          // **  Update is not working here (WHY?) **
          repo.query(no) match {

            //This is error translation
            case Right(Some(_)) => one(s"Ticket ($no) already exists").asLeft[Ticket]

            //This is the correct case
            case Right(None) =>
              if (no.isEmpty || title.isEmpty)
                one(s"Ticket open no and title required").asLeft[Ticket]
              else repo.store(Ticket(no, title, Open, Seq()))
                .left.map(error => NonEmptyList.of(s"Failed to open ticket ($no)", error))

            //Repository error
            case Left(error) =>
              NonEmptyList.of(s"Failed to open ticket ($no)", error).asLeft[Ticket]
          }
        }
      }
  }


  def start(no: String): TicketOperation[Ticket] = Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
    (repo: TicketRepository) =>
      EitherT {
        Future.successful {

          repo.update(no)(t => t.copy(status = InProgress).asRight)
            .leftMap( error => NonEmptyList.of(s"Failed to start ticket ($no)", error))

          // TODO remove kept here for educational purposes
//          repo.query(no) match {
//            case Right(None) => one(s"Ticket ($no) does not exist").asLeft[Ticket]
//
//            case Right(Some(t)) =>
//              val changed = t.copy(status = InProgress)
//              repo.store(changed)
//                .left.map(error => NonEmptyList.of(s"Failed to start ticket ($no)", error))
//
//            //Repository error
//            case Left(error) => NonEmptyList.of(s"Failed to start ticket ($no)", error).asLeft[Ticket]
//          }
        }
      }
  }

  def changeTitle(no: String, title: String): TicketOperation[Ticket] = Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
    (repo: TicketRepository) =>
      EitherT {
        Future.successful {
          repo.update(no)(t => t.copy(title = title).asRight)
              .leftMap( error => NonEmptyList.of(s"Failed to change title of ticket ($no)", error))

            // TODO remove kept here for educational purposes
//          repo.query(no) match {
//            case Right(None) => one(s"Ticket ($no) does not exist").asLeft[Ticket]
//
//            case Right(Some(t)) =>
//              val changed = t.copy(title = title)
//              repo.store(changed)
//                .left.map(error => NonEmptyList.of(s"Failed to change title of ticket ($no)", error))
//
//            //Repository error
//            case Left(error) => NonEmptyList.of(s"Failed to change title of ticket ($no)", error).asLeft[Ticket]
//          }
        }
      }
  }

  def close(no: String): TicketOperation[Ticket] = Kleisli[AsyncErrorOr, TicketRepository, Ticket] {
    (repo: TicketRepository) =>
      EitherT {
        Future.successful {

          repo.update(no)(t => t.copy(status = Closed).asRight)
            .leftMap( error => NonEmptyList.of(s"Failed to close ticket ($no)", error))

          // TODO remove kept here for educational purposes
//          repo.query(no) match {
//            case Right(None) => one(s"Ticket ($no) does not exist").asLeft[Ticket]
//
//            case Right(Some(t)) =>
//              val changed = t.copy(status = Closed)
//              repo.store(changed)
//                .left.map(error => NonEmptyList.of(s"Failed to close ticket ($no)", error))
//
//            //Repository error
//            case Left(error) => NonEmptyList.of(s"Failed to close ticket ($no)", error).asLeft[Ticket]
//          }
        }
      }
  }
}
