package repository

import cats.data.EitherNel
import model.AsyncErrorOr

trait Repository[K,V] {
  def query(key: K): AsyncErrorOr[Option[V]]
  def store(value: V): AsyncErrorOr[V]
  def update(key: K)(operation: V => EitherNel[String,V]): AsyncErrorOr[V]
}
