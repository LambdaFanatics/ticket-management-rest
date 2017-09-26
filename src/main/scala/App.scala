import java.util.Date

import repository.interpreter.{FailingTicketRepository, InMemoryTicketRepository}
import service.interpreter.TicketServiceInterpreter

// WHAT ABOUT THESE
import cats.instances.future._

import scala.concurrent.ExecutionContext.Implicits.global


object App extends App {

  val service = TicketServiceInterpreter


  // Create valid tickets
  val prog1 = for {
    _ <- service.open("1", "T1")
    _ <- service.open("2", "T2")
    last <- service.open("3", "T3")
  } yield last


  // Create one invalid ticket (no = 4)
  val prog2 = for {
    _ <- service.open("4", "")
  } yield ()


  def openStartAndUpdate(no: String) = for {
    t <- service.open(no, s"Ticket $no")
    now = new Date().toString
    t <- service.changeTitle(t.no, s" ${t.title} updated $now")
    t <- service.start(t.no)
  } yield t


  //FIXME write tests
  val repo = FailingTicketRepository // try also with `FailingTicketRepository`

  val allValidRes = prog1.run(repo) //Should update the repository

  val oneInvalidRes = prog2.run(repo) //Should return an error

  val start1 = openStartAndUpdate("1").run(repo) // Existing ticket should update
  val start4 = openStartAndUpdate("4").run(repo) // Non existing should execute



  println(s"create valid tickets res= $allValidRes")
  println(s"create one invalid ticket res= $oneInvalidRes")
  println(s"create and update ticket 1 res= $start1")
  println(s"create and update ticket 4 res= $start4")

//  println(repo.internal)

}
