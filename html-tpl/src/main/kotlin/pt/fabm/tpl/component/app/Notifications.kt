package pt.fabm.tpl.component.app

import pt.fabm.tpl.Component
import pt.fabm.tpl.LiteralClientImplementation
import pt.fabm.tpl.Type

class Notifications(type: Type) : Component("Notifications", type) {
  init {

    fun initTpl(init: Notifications.() -> Unit) {
      this.init()
    }

    fun eachNotification() = LiteralClientImplementation(
      """
      {uiStore.notifications.map((notification: Notification) => (
        <div
          key={notification.id}
          className={` alert alert-${'$'}{getClassNotificationType(notification.type)}`}
          role="alert"
        >
          {notification.content}
        </div>
      ))}
      
      """.trimIndent(), type
    )

    initTpl {
      div(className = "fixed-top container") {
        div(className = "float-right") {
          children += eachNotification()
        }
      }
    }
  }
}
