import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import repository.interpreter.db.{DatabaseLayer, DbTicketRepository}

import scala.concurrent.ExecutionContextExecutor

object Server extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val logger = Logging(system, getClass)

  val databaseLayer = DatabaseLayer(slick.jdbc.H2Profile, "database")

  logger.info("Populating database")
  //Recreate populate database
  databaseLayer.exec(databaseLayer.populate)


  //Initialize ticket repository
  val repo = DbTicketRepository(databaseLayer)

  // A request data DTO
  case class TicketData(title: String)

  //Sample rest http dsl usage
  val routes: Route = {
    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
    import io.circe.generic.auto._
    import service.interpreter.TicketServiceInterpreter._
    import model.Codecs._

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

  logger.info("Starting server")
  val (host, port) = ("localhost", 8700)
  val bindingFuture = Http().bindAndHandle(routes, host, port)

  bindingFuture.failed.foreach { ex =>
    logger.error(ex, "Failed to bind {}:{}", host, port)
  }
}


