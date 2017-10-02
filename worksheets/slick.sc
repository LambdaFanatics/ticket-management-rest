/** Expreriments of using slick */

import model.{Ticket, TicketStatus}
import repository.interpreter.db.tickets
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

val db: H2Profile.backend.DatabaseDef = Database.forURL("jdbc:h2:mem:ticketdb;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

val data = Seq(
  Ticket("1", "Ticket 1", TicketStatus.Open, Seq()),
  Ticket("2", "Ticket 1", TicketStatus.Open, Seq()),
  Ticket("100", "Ticket 1", TicketStatus.Open, Seq())
)

def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)


import scala.concurrent.ExecutionContext.Implicits.global

def populate: DBIO[Option[Int]] = {
  for {
    _ <- tickets.schema.drop.asTry andThen tickets.schema.create
    count <- tickets ++= data
  } yield count
}

def all: DBIO[Seq[Ticket]]= tickets.result

val q1 = tickets
  .filter( _.no === "1")
  .map(_.title)
  .update("FOO")



try {
  exec(populate)

  exec({q1 andThen all}.transactionally)
} catch  { case ex:Exception => ex}
finally db.close
