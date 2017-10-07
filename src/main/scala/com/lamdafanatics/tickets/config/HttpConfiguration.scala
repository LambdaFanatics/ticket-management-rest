package com.lamdafanatics.tickets.config

import akka.http.scaladsl.server.Directives._
import com.lamdafanatics.tickets.config.routes.TestRoutes

import scala.concurrent.ExecutionContext

class HttpConfiguration(implicit executionContext: ExecutionContext) {

  val testRoutes = new TestRoutes()

  val routes =
    pathPrefix("api" / "v1") {
      testRoutes.routes
    }
}
