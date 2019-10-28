package pt.fabm.tpl

import org.junit.jupiter.api.Test
import pt.fabm.tpl.test.*

class RenderTestElements {

  @Test
  fun testAppInput(){
    AppInput.render(
      label = "myLabel",
      type = "text",
      value = "value",
      appInputCreator = {AppInputServer(System.out)}
    )
  }

  @Test
  fun testApp() {
    App.render {
      AppServer(
        System.out, false, listOf(
          CarFields(
            maker = "{props.maker}",
            model = "{props.model}",
            matDate = "{props.maturityDate}",
            price = "{props.price}"
          )
        )
      )
    }
  }

  @Test
  fun testNabBar() {
    NavBar.render { NavBarServer(System.out, true) }
  }

  @Test
  fun testCarList() {
    val carListCreator = {
      CarListServer(
        System.out, true, listOf(
          CarFields(
            maker = "{props.maker}",
            model = "{props.model}",
            matDate = "{props.maturityDate}",
            price = "{props.price}"
          )
        )
      )
    }

    CarList.render(carListCreator)
  }


  @Test
  fun testCarView() {
    CarView.render(
      { CarViewServer(true, System.out) }, listOf(
        CarFields(
          maker = "{props.maker}",
          model = "{props.model}",
          matDate = "{props.maturityDate}",
          price = "{props.price}"
        ),
        CarFields(
          maker = "{props.maker1}",
          model = "{props.model1}",
          matDate = "{props.maturityDate1}",
          price = "{props.price1}"
        )
      )
    )
  }
}
