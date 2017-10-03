package repository.interpreter

import model.TicketStatus
import slick.jdbc.{JdbcProfile, JdbcType}
import slick.ast.BaseTypedType
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

package object db {

  trait Profile {
    val profile: slick.jdbc.JdbcProfile
  }

  implicit val ticketStatusColumn: JdbcType[TicketStatus] with BaseTypedType[TicketStatus] = MappedColumnType.base[TicketStatus, String](_.toString, {
    case "Open" => TicketStatus.Open
    case "InProgress" => TicketStatus.InProgress
    case _ => TicketStatus.Closed
  })

  case class DatabaseLayer(profile: JdbcProfile, configName: String) extends Profile with Tables {
    val db = profile.api.Database.forConfig(configName)

    val populate = for {
      _ <- tickets.schema.drop.asTry andThen tickets.schema.create
    } yield ()

    def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 2 seconds)
  }
}


