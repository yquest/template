package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.MultiEnvTemplateServer

class CarListServer(appendable: Buffer, private val auth: Boolean, private val cars: List<CarFields>) :
  CarList(appendable), MultiEnvTemplateServer {

  override fun ifHasCars(block: CarList.() -> Unit) {
    if(cars.isNotEmpty()) block()
  }
  override fun ifHasNoCars(block: CarList.() -> Unit) {
    if(cars.isEmpty()) block()
  }

  override fun colspanTr(colspan: Int): String = """ colspan="$colspan""""

  override fun carLines() {
    CarViewServer(auth, buffer).renderServer(cars)
  }
  override fun showIfAuthenticated(block: CarList.() -> Unit) {
    if (auth) block()
  }

}
