package repository

trait Repository[K,V] {
  def query(key: K): Either[String, Option[V]]
  def store(value: V): Either[String, V]
  def update(key: K)(operation: V => Either[String,V]): Either[String,V]
}
