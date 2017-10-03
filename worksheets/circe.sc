/** Experiments of using circe.io*/
import io.circe.{Decoder, Encoder}
import io.circe.syntax._

//Basic usage of cicre
val ints = List(1,2,3).asJson
val intsJs = ints.noSpaces

val tuple = (1,"2").asJson
val tupleJs = tuple.noSpaces

import io.circe.parser._
decode[(Int,String)](tupleJs).getOrElse((0,"")) //This avoids errors giving default value


//Encode
case class TicketData(title: String)

import io.circe.generic.semiauto._

implicit val ticketDataDecoder: Decoder[TicketData] = deriveDecoder
implicit val ticketDataEncoder: Encoder[TicketData] = deriveEncoder



val td = TicketData("Ticket 1")
val tdJs = td.asJson.noSpaces
decode[TicketData](tdJs).getOrElse(TicketData(""))

