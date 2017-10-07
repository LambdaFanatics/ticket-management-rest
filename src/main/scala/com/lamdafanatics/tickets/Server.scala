package com.lamdafanatics.tickets

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.lamdafanatics.tickets.config.{Config, DatabaseConfiguration, FlywayConfiguration, HttpConfiguration}

import scala.concurrent.ExecutionContext

object Server extends App with Config {
  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val logger: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  logger.info("Configuring database..")
  val database = new DatabaseConfiguration(jdbcUrl, dbUser, dbPassword)
  logger.info("Configuring flyway..")
  val flyway = new FlywayConfiguration(jdbcUrl, dbUser, dbPassword).migrateDatabaseSchema()
  logger.info("Configuring http..")
  val httpConfig = new HttpConfiguration()


  //  implicit val system: ActorSystem = ActorSystem()
  //  implicit val materializer: ActorMaterializer = ActorMaterializer()
  //  implicit val ec: ExecutionContextExecutor = system.dispatcher
  //
  //  val logger = Logging(system, getClass)
  //
  //  val databaseLayer = DatabaseLayer(slick.jdbc.H2Profile, "database")
  //
  //  logger.info("Populating database")
  //  //Recreate populate database
  //  databaseLayer.exec(databaseLayer.populate)
  //

  //Initialize ticket repository
//  val repo = DbTicketRepository(databaseLayer)

  // A request data DTO
//  case class TicketData(title: String)

  //Sample rest http dsl usage
//  val routes: Route = {
//    import service.interpreter.TicketServiceInterpreter._
//
//    pathPrefix("api" / "ticket") {
//      path(IntNumber / "open") { no =>
//        put {
//          entity(as[TicketData]) { input =>
//            complete {
//              open(no.toString, input.title)(repo).value
//            }
//
//          }
//        }
//      } ~ path(IntNumber / "start") { no =>
//        put {
//          complete {
//            start(no.toString)(repo).value
//          }
//        }
//      } ~ path(IntNumber / "title") { no =>
//        put {
//          entity(as[TicketData]) { input =>
//            complete {
//              changeTitle(no.toString, input.title)(repo).value
//            }
//          }
//        }
//      } ~ path(IntNumber / "close") { no =>
//        put {
//          complete(close(no.toString)(repo).value)
//        }
//      }
//    }
//  }

  logger.info("Starting Http server..")
  val httpBindAndHandle = Http().bindAndHandle(httpConfig.routes, host, port)
  httpBindAndHandle.failed.foreach { ex =>
    logger.error(ex, "Failed to bind {}:{}", host, port)
  }
}
