package com.lamdafanatics.tickets.domain

import cats.data.{EitherT, NonEmptyList}

import scala.concurrent.Future

package object domain {
  type Comments = Seq[Comment]

  case class Comment(c: String) extends AnyVal

  type AsyncErrorOr[A] = EitherT[Future, NonEmptyList[String], A]

}