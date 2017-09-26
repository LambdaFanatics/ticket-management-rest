package model

sealed trait TicketStatus
case object Open extends TicketStatus
case object InProgress extends TicketStatus
case object Closed extends TicketStatus

