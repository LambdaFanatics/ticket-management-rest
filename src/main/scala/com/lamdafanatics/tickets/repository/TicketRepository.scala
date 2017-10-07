package com.lamdafanatics.tickets.repository

import com.lamdafanatics.tickets.domain.ticket.Ticket

trait TicketRepository extends Repository[String, Ticket]