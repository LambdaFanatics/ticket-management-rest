package service.interpreter

import cats.data.NonEmptyList
import cats.instances.future._
import org.scalatest.{AsyncFunSpec, BeforeAndAfterEach, Matchers}
import repository.interpreter.{FailingTicketRepository, InMemoryTicketRepository}

class TicketServiceInterpreterSpec extends AsyncFunSpec with Matchers with BeforeAndAfterEach {

  private val service = TicketServiceInterpreter

  describe("TicketInterpreterService") {

    it("Should open single valid ticket") {
      val repo = InMemoryTicketRepository()
      val command = for {
       t <- service.open("1", "Ticket")
      } yield t
      command.run(repo)

      assert(repo.internal.size === 1)
      repo.internal.get("1").map(_.no) should be (Some("1"))
    }

    it("Should respond with an message on invalid ticket") {
      val repo = InMemoryTicketRepository()
      val command = for {
        t <- service.open("", "")
      } yield t
      val res = command.run(repo)

      assert(repo.internal.isEmpty)
      res.value.map(res => res should matchPattern{case Left(NonEmptyList(_,_)) => })
    }

    it("Should fail when repo fails") {
      val repo = FailingTicketRepository
      val command = for {
        t <- service.open("1", "T1")
      } yield t
      val res = command.run(repo)

      res.value.map(res => res should matchPattern{case Left(NonEmptyList(_,_)) => })
    }

    //TODO more tests ...
  }

}
