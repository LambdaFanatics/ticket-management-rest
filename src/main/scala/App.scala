import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContextExecutor

object App extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val logger = Logging(system, getClass)

  val handler: Route = get {
    complete(ToResponseMarshallable.apply("Hello world"))
  }

  val (host, port) = ("localhost", 8700)
  val bindingFuture = Http().bindAndHandle(handler, host, port)

  bindingFuture.failed.foreach { ex =>
    logger.error(ex, "Failed to bind {}:{}", host, port)
  }
}


