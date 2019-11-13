package pt.fabm.tpl

import io.vertx.core.buffer.Buffer
import org.junit.jupiter.api.Test
import pt.fabm.tpl.component.AppInput
import pt.fabm.tpl.component.AppInputServer
import pt.fabm.tpl.component.DropDowInputClient
import pt.fabm.tpl.component.DropDowInputServer
import pt.fabm.tpl.component.app.*

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
    Buffer.buffer().let { LoginClient(it).renderImplementation();println(it.toString()) }
  }

  @Test
  fun testLoginServer() {
    Buffer.buffer().let { LoginServer(it, true).render();println(it.toString()) }
  }

  @Test
  fun testAppInputServer() {
    Buffer.buffer().let {
      AppInputServer(appendable = it, type = AppInput.Type.TEXT, value = "")
        .render("myLabel");println(it.toString())
    }
  }

  @Test
  fun testAppServer() {
    Buffer.buffer().let {  AppServer(it, false, carListServer).render();println(it.toString())}
  }

  @Test
  fun testAppClient() {
    Buffer.buffer().let {  AppClient(it).render();println(it.toString())}
  }

  @Test
  fun testNabBarServer() {
    Buffer.buffer().let {  NavBarServer(it, true).render();println(it.toString())}
  }

  @Test
  fun testNabBarClient() {
    Buffer.buffer().let {  NavBarClient(it).render();println(it.toString())}
  }

  @Test
  fun testCarListServer() {
    Buffer.buffer().let {  CarListServer(it, true, carListServer).render()}
  }

  @Test
  fun testCarListClient() {
    Buffer.buffer().let {  CarListClient(it).render();println(it.toString())}
  }

  @Test
  fun testCarListClientImplementation() {
    Buffer.buffer().let {  CarListClient(it).renderImplementation();println(it.toString())}
  }

  @Test
  fun testCarViewServer() {
    Buffer.buffer().let {  CarViewServer(true, it).renderServer(carListServer);println(it.toString())}
  }

  @Test
  fun testCarViewClient() {
    Buffer.buffer().let {  CarViewClient(it).render();println(it.toString())}
  }

  @Test
  fun testRegisterUser() {
    Buffer.buffer().let {  RegisterUserClient(it).render();println(it.toString())}
  }

  @Test
  fun testDropDown() {
    Buffer.buffer().let {  DropDowInputServer(it, "dd1", false, listOf("element 1", "element 2"))
      .render(
        label = "drop down 1",
        tabIndex = 0,
        btnLabel = "btl lb",
        value = 0
      );println(it.toString())}

    println()

      Buffer.buffer().let {  DropDowInputClient(it).render(
      label = "{props.label}",
      tabIndex = "props.tabIndex",
      btnLabel = "{props.btnLabel}",
      value = "{props.inputValue}"
    );println(it.toString())}
  }

  @Test
  fun testCarviewClient() {
    Buffer.buffer().let {  CarViewClient(it).renderImplementation();println(it.toString())}
  }

}
