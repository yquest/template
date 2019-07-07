package pt.fabm.template.dao

import java.sql.ResultSet

fun <T> ResultSet.iterable(converter: (ResultSet) -> T):Iterable<T> {
  val iterator = object : Iterator<T> {
    override fun hasNext(): Boolean = this@iterable.next()
    override fun next(): T = converter(this@iterable)
  }
  return object : Iterable<T> {
    override fun iterator(): Iterator<T> = iterator
  }
}
