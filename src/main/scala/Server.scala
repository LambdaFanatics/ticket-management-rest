import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import repository.interpreter.InMemoryTicketRepository

import scala.concurrent.ExecutionContextExecutor

object Server extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val logger = Logging(system, getClass)

  //Initialize the in memory repository
  val repo = InMemoryTicketRepository()



  //TODO move this for here (avoid DTOs?)
  case class TicketData(title: String)

  //Sample rest http dsl usage
  val routes: Route = {
    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
    import io.circe.generic.auto._
    import service.interpreter.TicketServiceInterpreter._


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

  val (host, port) = ("localhost", 8700)
  val bindingFuture = Http().bindAndHandle(routes, host, port)

  bindingFuture.failed.foreach { ex =>
    logger.error(ex, "Failed to bind {}:{}", host, port)
  }
}


