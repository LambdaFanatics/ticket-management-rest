package service.interpreter

import cats.data.NonEmptyList
import cats.instances.future._
import model.{Ticket, TicketStatus}
import org.scalatest.{AsyncFunSpec, FunSpec, Matchers}
import repository.interpreter.{FailingTicketRepository, InMemoryTicketRepository}


class TicketServiceInterpreterSpec extends AsyncFunSpec with Matchers {

  private val service = TicketServiceInterpreter

  describe("TicketInterpreterService") {

    it("Should open single valid ticket") {
      val repo = InMemoryTicketRepository()
      val command = for {
        t <- service.open("1", "Ticket")
      } yield t
      command.run(repo)

      repo.internal.get("1").map(_.no) should be(Some("1"))
    }

    it("Should fail with errors on invalid ticket") {
      val repo = InMemoryTicketRepository()
      val command = for {
        t <- service.open("", "")
      } yield t
      val res = command.run(repo)


      res.value
        .map(
          res => res should matchPattern { case Left(NonEmptyList("Failed to open ticket ()", Seq("Ticket open no and title required"))) => }
        )
        .map(_ => assert(repo.internal.isEmpty))
    }

    it("Should fail when repo fails") {
      val repo = FailingTicketRepository
      val command = for {
        t <- service.open("1", "T1")
      } yield t
      val res = command.run(repo)

      res.value.map(res =>
        res should matchPattern { case Left(NonEmptyList("Failed to open ticket (1)", Seq("Storage unavailable"))) => }
      )

    }

    it("Should open and start a valid ticket") {
      val repo = InMemoryTicketRepository()
      val command = for {
        t <- service.open("1", "T1")
        opened <- service.start(t.no)
      } yield opened
      val res = command.run(repo)



      res.value
        .map(res => res should be(Right(Ticket("1", "T1", TicketStatus.InProgress, Seq()))))
        .map(_ => assert(repo.internal.nonEmpty))
    }

    it("Should open and start and close a valid ticket") {
      val repo = InMemoryTicketRepository()
      val command = for {
        t <- service.open("1", "T1")
        opened <- service.start(t.no)
        closed <- service.close(opened.no)
      } yield closed
      val res = command.run(repo)

      res.value
        .map(res => res should be(Right(Ticket("1", "T1", TicketStatus.Closed, Seq()))))
        .map(_ => assert(repo.internal.nonEmpty))
    }


    it("Should fail with errors when trying to close non existed ticket") {
      val repo = InMemoryTicketRepository()
      val command = for {
        opened <- service.changeTitle("2", "CHANGE")
      } yield opened
      val res = command.run(repo)

      res.value
        .map(res => {
          res should matchPattern { case Left(NonEmptyList("Failed to change title of ticket (2)", Seq("Ticket (2) does not exist"))) => }
        })
        .map(_ => assert(repo.internal.isEmpty))

    }

    //Here more tests ... you are welcome to try!
  }

}
