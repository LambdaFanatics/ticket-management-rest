package model

import io.circe._

object Codecs {

  implicit val ticketStatusEncoder: Encoder[TicketStatus] = new Encoder[TicketStatus] {
    override def apply(ts: TicketStatus): Json = ts match {
      case status@_ => Json.fromString(status.toString)
    }
  }
}
