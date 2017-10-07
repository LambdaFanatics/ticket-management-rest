package com.lamdafanatics.tickets.service

import cats.data.Kleisli
import com.lamdafanatics.tickets.domain.ticket.Ticket

trait TicketService {
  type TicketOperation[A] = Kleisli[AsyncErrorOr, TicketRepository, A]

  def open(no: String, title: String): TicketOperation[Ticket]
  def start(no: String): TicketOperation[Ticket]
  def changeTitle(no: String, title: String): TicketOperation[Ticket]
  def close(no: String): TicketOperation[Ticket]
}