import repository.interpreter.db.DatabaseLayer

/** Expreriments of using slick */

import model.{Ticket, TicketStatus}
import scala.concurrent.Await
import scala.concurrent.duration._


val dbLayer = DatabaseLayer(slick.jdbc.H2Profile)
import dbLayer._ ,profile.api._

val db = Database.forURL("jdbc:h2:mem:ticketdb;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

val data = Seq(
  Ticket("1", "Ticket 1", TicketStatus.Open, Seq()),
  Ticket("2", "Ticket 1", TicketStatus.Open, Seq()),
  Ticket("100", "Ticket 1", TicketStatus.Open, Seq())
)

import scala.concurrent.ExecutionContext.Implicits.global

val populate = for {
  _ <- tickets.schema.drop.asTry andThen tickets.schema.create
  _ <- tickets ++=  data
  ts <- tickets.result
} yield ts



try {
  Await.result(db.run(populate), 2 seconds)
} catch  { case ex:Exception => ex}
finally db.close



