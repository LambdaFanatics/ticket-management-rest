import repository.interpreter.{InMemoryFailingTicketRepository, InMemoryTicketRepository}
import service.interpreter.TicketServiceInterpreter

import cats._
import cats.data._
import cats.implicits._
import cats.instances._

object App extends App {
  val memoryRepo = InMemoryTicketRepository
  val failingRepo = InMemoryFailingTicketRepository

  val service = TicketServiceInterpreter

  val res = service.open("", "T1").run(memoryRepo)

//  val createTickets = for {
//    _ <- service.open("1","T1")
//    _ <- service.open("2","T2")
//    _ <- service.open("3","T3")
//  } yield ()


//  createTickets.run(memoryRepo)

  println(res)

  println(memoryRepo.internal)

}
