package service

import cats.data.Kleisli
import model.{Ticket, AsyncErrorOr}
import repository.TicketRepository

trait TicketService {
  type TicketOperation[A] = Kleisli[AsyncErrorOr, TicketRepository, A]


  def findAll(): TicketOperation[Seq[Ticket]]
  def open(no: String, title: String): TicketOperation[Ticket]
  def start(no: String): TicketOperation[Ticket]
  def changeTitle(no: String, title: String): TicketOperation[Ticket]
  def close(no: String): TicketOperation[Ticket]
}
