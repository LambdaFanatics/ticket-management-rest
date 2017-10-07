package com.lamdafanatics.tickets.config.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.concurrent.ExecutionContext

class TicketRoutes(implicit executionContext: ExecutionContext) extends FailFastCirceSupport {

  //Initialize ticket repository
  val repo = DbTicketRepository(databaseLayer)

  // A request data DTO
  case class TicketData(title: String)

  val routes = {
    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
    import io.circe.generic.auto._
    import com.lamdafanatics.tickets.service.TicketServiceImpl._
//    import com.lamdafanatics.tickets.domain.domain.Codecs._

    pathPrefix("api" / "ticket") {
      path(IntNumber / "open") { no =>
        put {
          entity(as[TicketData]) { input =>
            complete {
              open(no.toString, input.title)(repo).value
            }

          }
        }
      } ~ path(IntNumber / "start") { no =>
        put {
          complete {
            start(no.toString)(repo).value
          }
        }
      } ~ path(IntNumber / "title") { no =>
        put {
          entity(as[TicketData]) { input =>
            complete {
              changeTitle(no.toString, input.title)(repo).value
            }
          }
        }
      } ~ path(IntNumber / "close") { no =>
        put {
          complete(close(no.toString)(repo).value)
        }
      }
    }
  }

}
