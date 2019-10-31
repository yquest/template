package pt.fabm.tpl

import org.junit.jupiter.api.Test
import pt.fabm.tpl.test.*

class RenderTestElements {

  val carListServer = listOf(1, 2, 3, 4).map {
    CarFields(
      maker = "maker server $it",
      model = "model server $it",
      matDate = "maturity date server $it",
      price = "price server $it"
    )
  }

  @Test
  fun testLoginClient() {
    Login.render { LoginClient(System.out) }
  }

  @Test
  fun testLoginServer() {
    Login.render { LoginServer(System.out,true) }
  }

  @Test
  fun testAppInput() {
    AppInput.render(
      label = "myLabel",
      type = AppInput.Type.TEXT,
      value = "value",
      appInputCreator = { AppInputServer(System.out) }
    )
  }

  @Test
  fun testApp() {
    App.render { AppServer(System.out, false, carListServer) }
  }

  @Test
  fun testNabBar() {
    NavBar.render { NavBarServer(System.out, true) }
  }

  @Test
  fun testCarListServer() {
    CarList.render { CarListServer(System.out, true, carListServer) }
  }

  @Test
  fun testCarListClient() {
    CarList.render { CarListClient(System.out) }
  }

  @Test
  fun testCarListClientImplementation() {
    CarListClient(System.out).renderImplementation()
  }

  @Test
  fun testCarView() {
    CarView.render({ CarViewServer(true, System.out) }, carListServer)
  }
}
