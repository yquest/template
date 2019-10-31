package pt.fabm.tpl.test

class CarListServer(appendable: Appendable, private val auth: Boolean, private val cars: List<CarFields>) :
  CarList(appendable) {
  override val attributesBuilder: AttributesBuilder = AttributesBuilderServer()
  override fun ifHasCars(block: CarList.() -> Unit) {
    if(cars.isNotEmpty()) block()
  }

  override fun ifHasNoCars(block: CarList.() -> Unit) {
    if(cars.isEmpty()) block()
  }

  override fun colspanTr(colspan: Int): String = """ colspan="$colspan""""
  override fun carLines() {
    CarView.render({ CarViewServer(auth, appendable) }, cars)
  }

  override fun showIfAuthenticated(block: CarList.() -> Unit) {
    if (auth) block()
  }

}
