package repository

import model.Ticket

trait TicketRepository extends Repository[String, Ticket]
