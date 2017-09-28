import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import scala.concurrent.ExecutionContextExecutor

object Server extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val logger = Logging(system, getClass)


  //TODO move this for here (avoid DTOs?)
  case class TicketData(title: String)

  //Sample rest http dsl usage
  val routes: Route = {
    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
    import io.circe.generic.auto._

    pathPrefix("api" / "ticket") {
      path(IntNumber / "open") { no =>
        put {
          entity(as[TicketData]) { input =>
            complete(s"ticket ($no, '${input.title}') opened.")
          }
        }
      } ~ path(IntNumber / "start") { no =>
        put {
          complete(s"ticket ($no) started.")
        }
      } ~ path(IntNumber / "title") { no =>
        put {
          entity(as[TicketData]) { input =>
            complete(s"ticket ($no) title changed to '${input.title}'.")
          }
        }
      } ~ path(IntNumber / "close") { no =>
        put {
          complete(s"ticket ($no) closed.")
        }
      }
    }
  }

  val (host, port) = ("localhost", 8700)
  val bindingFuture = Http().bindAndHandle(routes, host, port)

  bindingFuture.failed.foreach { ex =>
    logger.error(ex, "Failed to bind {}:{}", host, port)
  }
}


