package com.lamdafanatics.tickets.config.routes

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.concurrent.ExecutionContext

class TestRoutes(implicit executionContext: ExecutionContext) extends FailFastCirceSupport {

  val routes =
    get {
      path("test" / "ping") {
        complete("PONG!")
      } ~
        path("test" / "crash") {
          sys.error("BOOM!")
        }
    }

}
