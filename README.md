###postges:
````docker run --name postgres \                                                                                                                                    130 ↵
      -e POSTGRES_PASSWORD=postgres \
      -v /opt/postgres/data:/var/lib/postgresql/data \
      -p 5432:5432 \
      -d postgres
````

####Ticket management system 
A ticket management system sample application.

This application demonstrates the use of 
__slick__, __akka-http__ and  __cats__ library in a simple rest application.

The implementation of the application  influenced by the material of the books:
* [Functional Programming in Scala](https://www.manning.com/books/functional-programming-in-scala)
* [Functional and Reactive Domain Modeling](https://www.manning.com/books/functional-and-reactive-domain-modeling)
* [Essential Slick](http://books.underscore.io/essential-slick/essential-slick-3.html)

Also consulting the documentation of the following libraries will be helpfull:
* [akka-http](https://doc.akka.io/docs/akka-http/current/scala/http/)
* [slick](http://slick.lightbend.com/docs/)
* [cats](https://typelevel.org/cats/)
    
#### Tags
This git com.lamdafanatics.tickets.repository contains tags which snapshot the creation process of the project.

This is done for educational purposes. 

Navigate through the tags (via `git checkout`) and study the code.

The available __tags__ are:

* __step1__: Initial scaffolding and base project structure. 
Contains algebra implementation with in memory and failing com.lamdafanatics.tickets.repository

* __step2__: Reviewing the com.lamdafanatics.tickets.repository api and improving it (see `TicketServiceInterpreter.scala`)

* __step3__: Introducing akka-http (see `logback.xml`, `application.conf`, `build.sbt`)

* __step4__: Creating rest api interface  using akka-http (see `Server.scala` and `worksheets/circe.sc`) 

* __step5__: Integrating the ticket com.lamdafanatics.tickets.service using an in memory com.lamdafanatics.tickets.repository (see `Server.scala`)

* __step6__: Introducing slick with H2Database drivers (see `slick.sc`, `build.sbt`).

* __step7__: Refactoring com.lamdafanatics.tickets.repository and com.lamdafanatics.tickets.service API (see `Repository`, `TicketServiceInterpreter`, `FailingTicketRepository`, `InMemoryTicketRepository`).

* __step8__: Implementing the database com.lamdafanatics.tickets.repository using slick (see `DbTicketRepository`)

 _Fotios Paschos, `@fpaschos`, Oct 2017_