package model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import model.Errors.GenericError
import model.request.TicketRequest
import spray.json.{DefaultJsonProtocol, JsNumber, JsObject, JsString, JsValue, JsonFormat}


object Codecs extends SprayJsonSupport with DefaultJsonProtocol {

  implicit def ticketStatusFormat: JsonFormat[TicketStatus] = new JsonFormat[TicketStatus] {
    override def write(obj: TicketStatus) = JsString(obj.toString)

    override def read(json: JsValue): TicketStatus = json match {
      case JsString("Open") => TicketStatus.Open
      case JsString("InProgress") => TicketStatus.InProgress
      case JsString("Closed") => TicketStatus.Closed
      case _ => spray.json.deserializationError(json.toString + " is not a string")
    }
  }

  implicit val commendFormat: JsonFormat[Comment] = jsonFormat1(Comment)

  implicit val ticketFormat: JsonFormat[Ticket] = jsonFormat4(Ticket)

  implicit val ticketDataFormat = jsonFormat1(TicketRequest)

  implicit def errorStatusFormat: JsonFormat[Errors.Error] = new JsonFormat[Errors.Error] {
    override def write(o: Errors.Error) = JsObject(
      ("code", JsNumber(o.code)),
      ("message", JsString(o.message))
    )

    override def read(json: JsValue) = GenericError
  }


}
