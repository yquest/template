package pt.fabm.tpl

import org.junit.jupiter.api.Test
import pt.fabm.tpl.component.*
import pt.fabm.tpl.component.app.*
import java.io.FileWriter
import kotlin.text.Appendable

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
    LoginClient(System.out).renderImplementation()
  }

  @Test
  fun testLoginServer() {
    LoginServer(System.out, true).render()
  }

  @Test
  fun testAppInputServer() {
    AppInputServer(appendable = System.out,type = AppInput.Type.TEXT, value = "")
      .render("myLabel")
  }

  @Test
  fun testAppServer() {
    AppServer(System.out, false, carListServer).render()
  }

  @Test
  fun testAppClient() {
    AppClient(System.out).render()
  }

  @Test
  fun testNabBarServer() {
    NavBarServer(System.out, true).render()
  }

  @Test
  fun testNabBarClient() {
    NavBarClient(System.out).render()
  }

  @Test
  fun testCarListServer() {
    CarListServer(System.out, true, carListServer).render()
  }

  @Test
  fun testCarListClient() {
    CarListClient(System.out).render()
  }

  @Test
  fun testCarListClientImplementation() {
    CarListClient(System.out).renderImplementation()
  }

  @Test
  fun testCarViewServer() {
    CarViewServer(true, System.out).renderServer(carListServer)
  }

  @Test
  fun testCarViewClient() {
    CarViewClient(System.out).render()
  }

  @Test
  fun testRegisterUser() {
    RegisterUserClient(System.out).render()
  }

  @Test
  fun testDropDown(){
    DropDowInputServer(System.out, "dd1", false, listOf("element 1", "element 2"))
      .render(
        label = "drop down 1",
        tabIndex = 0,
        btnLabel = "btl lb",
        value = 0
      )

    println()

    DropDowInputClient(System.out).render(
      label = "{props.label}",
      tabIndex = "props.tabIndex",
      btnLabel = "{props.btnLabel}",
      value = "{props.inputValue}"
    )
  }

  @Test
  fun testCarviewClient() {
    CarViewClient(System.out).renderImplementation()
  }

}
