package pt.fabm.tpl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarMake
import pt.fabm.tpl.component.app.App
import pt.fabm.tpl.component.car.CarList
import java.time.LocalDateTime


class RenderTests {
  companion object {
    private val defaultCarList = listOf(
      Car(model = "model", maturityDate = LocalDateTime.of(2019, 1, 1, 1, 1), make = CarMake.VOLKSWAGEN, price = 200)
    )
  }

  private fun createNotifications(type: Type): Component {
    class Notifications(type: Type) : Component("Notifications", type) {
      init {
        children += object : ElementCreator,WithChildren {
          override val type: Type get() = type
          override val children: MutableList<ElementCreator> = mutableListOf()
          override fun create(): Element {
            if (type == Type.CLIENT_IMPLEMENTATION) return TextElement(
              """
            @observer
            export class Notifications extends React.Component<{}, {}> {
              render() {
                return (
                  <div className="fixed-top container">
                    <div className="float-right">
                      {uiStore.notifications.map((notification: Notification) => (
                        <div
                          key={notification.id}
                          className={` alert alert-${'$'}{getClassNotificationType(notification.type)}`}
                          role="alert"
                        >
                          {notification.content}
                        </div>
                      ))}
                    </div>
                  </div>
                );
              }
            }
            """.trimIndent()
            )
            else return NoTagElement()
          }
        }
      }
    }
    return Notifications(type)
  }

  @Test
  fun renderCarList() {
    val carListClientImplementation = CarList(
      Type.CLIENT_IMPLEMENTATION,
      true,
      defaultCarList
    )
    val carListServer = CarList(
      Type.SERVER,
      true,
      defaultCarList
    )
    val carListServerRenderer = carListServer.create()

    var current = StringBuilder().let { carListClientImplementation.create().renderTag(it);it.toString() }
    var expected = """
    const noContent = () => (
        <div>
            no cars available
        </div>
    );
    const content = (props: carList.Props) => (
        <table>
            <thead>
                <tr>
                    <th>
                        Make
                    </th>
                    <th>
                        Model
                    </th>
                    <th>
                        Maturity date
                    </th>
                    <th>
                        Price
                    </th>
                    {props.authenticated && (
                        <th colSpan={2}>
                            Actions
                        </th>
                    )}
                </tr>
            </thead>
            <tbody>
                {props.cars.map((car, idx) => (
                    <CarView authenticated={props.authenticated} key={idx} maker={MAKERS[car.make]} model={car.model} maturityDate={dateToStringReadable(car.maturityDate)} price={car.price + "€"} carManager={props.carManagerCreator(car)} car={car}></CarView>
                ))}
            </tbody>
        </table>
    );
    export const CarList = (props: carList.Props) => 
      props.cars.length === 0 ? noContent() : content(props);
    
    """.trimIndent()
    Assertions.assertEquals(expected, current)

    current = StringBuilder().let { CarList(Type.CLIENT, true, defaultCarList).create().renderTag(it);it.toString() }
    expected = """ 
      <CarList cars={props.cars} authenticated={props.authenticated} carManagerCreator={props.carManagerCreator}></CarList>
      
    """.trimIndent()
    Assertions.assertEquals(expected, current)

    current = StringBuilder().let { carListServerRenderer.renderTag(it);it.toString() }
    expected = """
    <table>
        <thead>
            <tr>
                <th>
                    Make
                </th>
                <th>
                    Model
                </th>
                <th>
                    Maturity date
                </th>
                <th>
                    Price
                </th>
                    <th colspan="2">
                        Actions
                    </th>
            </tr>
        </thead>
        <tbody>
            <!--start each CarView-->
            <tr>
                <td>
                    VOLKSWAGEN
                </td>
                <td>
                    model
                </td>
                <td>
                    2019-01-01T01:01
                </td>
                <td>
                    200
                </td>
                    <!--only authenticated-->
                    <td>
                        <a href="javascript:void(0)" class="btn"></a>
                    </td>
                    <!--only authenticated-->
                    <td>
                        <a href="javascript:void(0)" class="btn"></a>
                    </td>
            </tr>
            <!--end each CarView-->
        </tbody>
    </table>
    
    """.trimIndent()
    Assertions.assertEquals(expected, current)

    carListServer.carEdit = false
    carListServer.list = defaultCarList + listOf(
      Car(model = "Note", make = CarMake.NISSAN, price = 15000, maturityDate = LocalDateTime.of(2019, 2, 2, 2, 2))
    )
    current = StringBuilder().let { carListServerRenderer.renderTag(it);it.toString() }
    expected = """
    <table>
        <thead>
            <tr>
                <th>
                    Make
                </th>
                <th>
                    Model
                </th>
                <th>
                    Maturity date
                </th>
                <th>
                    Price
                </th>
                    <th colspan="2">
                        Actions
                    </th>
            </tr>
        </thead>
        <tbody>
            <!--start each CarView-->
            <tr>
                <td>
                    VOLKSWAGEN
                </td>
                <td>
                    model
                </td>
                <td>
                    2019-01-01T01:01
                </td>
                <td>
                    200
                </td>
            </tr>
            <tr>
                <td>
                    NISSAN
                </td>
                <td>
                    Note
                </td>
                <td>
                    2019-02-02T02:02
                </td>
                <td>
                    15000
                </td>
            </tr>
            <!--end each CarView-->
        </tbody>
    </table>
    
    """.trimIndent()
    Assertions.assertEquals(expected, current)

    carListServer.carEdit = false
    carListServer.list = emptyList()
    current = StringBuilder().let { carListServerRenderer.renderTag(it);it.toString() }
    expected = """
    <div>
        no cars available
    </div>
    
    """.trimIndent()
    Assertions.assertEquals(expected, current)
  }

  @Test
  fun renderNotifications() {
    var current = StringBuilder().let { createNotifications(Type.CLIENT).create().renderTag(it);it.toString() }
    var expected = "<Notifications></Notifications>\n"
    Assertions.assertEquals(expected, current)
    current = StringBuilder()
      .let { createNotifications(Type.CLIENT_IMPLEMENTATION).create().renderTag(it);it.toString() }
    expected = """
    @observer
    export class Notifications extends React.Component<{}, {}> {
      render() {
        return (
          <div className="fixed-top container">
            <div className="float-right">
              {uiStore.notifications.map((notification: Notification) => (
                <div
                  key={notification.id}
                  className={` alert alert-${'$'}{getClassNotificationType(notification.type)}`}
                  role="alert"
                >
                  {notification.content}
                </div>
              ))}
            </div>
          </div>
        );
      }
    }
    
    """.trimIndent()
    Assertions.assertEquals(expected, current)
    current = StringBuilder()
      .let { createNotifications(Type.SERVER).create().renderTag(it);it.toString() }.trim()
    Assertions.assertTrue(current.isEmpty())
  }

  @Test
  fun renderApp() {
    var current = StringBuilder().let {
      App(type = Type.CLIENT, carEdit = true, auth = true, carList = defaultCarList, username = { "rockMyWorld" })
        .create()
        .renderTag(it)
      it.toString()
    }
    var expected = """
    <div className="container app">
        {props.appState === app.AppState.LIST_NO_AUTH && (
            <a href="javascript:void();" onClick={props.loginOn}>
                Sign in
                <i className="fas fa-sign-in-alt"></i>
            </a>
        )}
        <Notifications></Notifications>
        {props.appState === app.AppState.CAR_EDIT_AUTH && (
            <div key="helloUsername" className="float-right">
                Hello {props.username + " "}
                <a href="javascript:void(0)" onClick={props.loginOff}>
                    logoff
                    <i className="fas fa-sign-out-alt"></i>
                </a>
            </div>
        )}
        {(props.appState === app.AppState.LIST_NO_AUTH || props.appState === app.AppState.CAR_EDIT_AUTH) && (
            <CarList cars={props.cars} authenticated={props.authenticated} carManagerCreator={props.carManagerCreator}></CarList>
        )}
    </div>
    
    """.trimIndent()
    Assertions.assertEquals(expected, current)

    current = StringBuilder().let {
      App(type = Type.SERVER, carEdit = true, auth = true, carList = defaultCarList, username = { "rockMyWorld" })
        .create()
        .renderTag(it)
      it.toString()
    }
    expected = """
    <div class="container app">
        <div class="fixed-top container">
            <div class="float-right">
            </div>
        </div>
            <div key="helloUsername" class="float-right">
                Hello rockMyWorld 
                <a href="javascript:void(0)">
                    logoff
                    <i class="fas fa-sign-out-alt"></i>
                </a>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>
                            Make
                        </th>
                        <th>
                            Model
                        </th>
                        <th>
                            Maturity date
                        </th>
                        <th>
                            Price
                        </th>
                            <th colspan="2">
                                Actions
                            </th>
                    </tr>
                </thead>
                <tbody>
                    <!--start each CarView-->
                    <tr>
                        <td>
                            VOLKSWAGEN
                        </td>
                        <td>
                            model
                        </td>
                        <td>
                            2019-01-01T01:01
                        </td>
                        <td>
                            200
                        </td>
                            <!--only authenticated-->
                            <td>
                                <a href="javascript:void(0)" class="btn"></a>
                            </td>
                            <!--only authenticated-->
                            <td>
                                <a href="javascript:void(0)" class="btn"></a>
                            </td>
                    </tr>
                    <!--end each CarView-->
                </tbody>
            </table>
    </div>
    
    """.trimIndent()
    Assertions.assertEquals(expected, current)

  }
}
