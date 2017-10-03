package repository

import cats.data.EitherNel
import model.AsyncErrorOr
import model.Errors.Error

trait Repository[K,V] {
  def findAll(): AsyncErrorOr[Seq[V]]
  def query(key: K): AsyncErrorOr[Option[V]]
  def store(value: V): AsyncErrorOr[V]


  def update(key: K)(operation: V => EitherNel[Error,V]): AsyncErrorOr[V]
}
