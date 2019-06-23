package pt.fabm.template.dao

import java.sql.ResultSet

class ResultSetIterator<T>(val rs:ResultSet, val converter:(ResultSet)->T) : Iterable<T>, Iterator<T> {
  override fun iterator(): Iterator<T> = this
  override fun hasNext(): Boolean = rs.next()
  override fun next(): T = converter(rs)
}
