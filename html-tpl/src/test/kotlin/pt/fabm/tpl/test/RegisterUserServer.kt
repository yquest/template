package pt.fabm.tpl.test

class RegisterUserServer(appendable: Appendable, private val auth: Boolean) : RegisterUser(appendable) {
  override fun modal() {
    //ignore
  }

  override fun navbar() {
    NavBarServer(appendable, auth).render()
  }

  override fun notifications() {
    //ignore
  }

  override fun appInput(label: String, tabIndex: Int, type: AppInput.Type) {
    val appInput = AppInputServer(appendable)
    fun inputRender(label: String, type: AppInput.Type, tabIndex: Int){
      appInput.render(
        label=label,
        type = type,
        tabIndex = tabIndex.toString(),
        value = ""
      )
    }


    when (label) {
      Fields.USERNAME -> inputRender("Username",AppInput.Type.TEXT,1)
      Fields.PASSWORD -> inputRender("Password",AppInput.Type.TEXT,2)
      Fields.EMAIL -> inputRender("Email",AppInput.Type.TEXT, tabIndex = 3)
    }
  }

  override fun modalBackground() {
    //ignore
  }

  override fun appendClient(text: String) {
    //ignore
  }

  override fun appendServer(text: String) {
    appendBody(text)
  }

}
