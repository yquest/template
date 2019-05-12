package pt.fabm.template.models

data class SimpleDate(
  val year: Int,
  val month: Int,
  val day: Int,
  val hour: Int,
  val minute: Int
) {

  override fun toString(): String {
    fun Int.pad2(): String {
      return this.toString().padStart(2, '0')
    }
    return "$year-${month.pad2()}-${day.pad2()}T${hour.pad2()}:${minute.pad2()}:00"
  }
}
