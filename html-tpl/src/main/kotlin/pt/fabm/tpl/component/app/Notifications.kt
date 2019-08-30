package pt.fabm.tpl.component.app

import pt.fabm.tpl.*

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
