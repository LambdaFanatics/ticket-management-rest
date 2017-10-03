import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import model.{Comment, Ticket, TicketStatus}
import model.request.TicketRequest
import repository.interpreter.db.{DatabaseLayer, DbTicketRepository}

import scala.concurrent.{ExecutionContextExecutor}
import spray.json._
import model.Codecs._



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



  //Sample rest http dsl usage
  val routes: Route = {





    import service.interpreter.TicketServiceInterpreter._
    import cats.syntax.either._

    pathPrefix("api" / "ticket") {
      pathEnd {
        get {
          complete {

            findAll()(repo).value.map(_.leftMap(_.toList).toJson)
          }
        }
      } ~ path(IntNumber / "start") { no =>
        put {
          complete {
            start(no.toString)(repo).value.map(_.leftMap(_.toList).toJson)
          }
        }
      } ~ path(IntNumber / "open") { no =>
        put {
          entity(as[TicketRequest]) { input =>
            complete {
              open(no.toString, input.title)(repo).value.map(_.leftMap(_.toList).toJson)
            }
          }
        }
      } ~ path(IntNumber / "title") { no =>
        put {
          entity(as[TicketRequest]) { input =>
            complete {
              changeTitle(no.toString, input.title)(repo).value.map(_.leftMap(_.toList).toJson)
            }
          }
        }
      } ~ path(IntNumber / "close") { no =>
        put {
          complete(close(no.toString)(repo).value.map(_.leftMap(_.toList).toJson))
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


