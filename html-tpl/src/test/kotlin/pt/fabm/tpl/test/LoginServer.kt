package pt.fabm.tpl.test

class LoginServer(appendable: Appendable, private val auth: Boolean) : Login(appendable) {

  override fun asClientText(text: String): String? = null
  override fun literalClassName(value: String): String = ""
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
    fun renderInput(label: String, type: AppInput.Type, tabIndex: Int){
      appInput.render(
        label= label,
        type = type,
        tabIndex = tabIndex.toString(),
        value = ""
      )
    }
    when (label) {
      Fields.LOGIN -> renderInput("Login", AppInput.Type.TEXT, 1)
      Fields.PASSWORD -> renderInput("Password", AppInput.Type.PASSWORD,2)
    }
  }

  override fun clientText(text: String) {
    //ignore
  }

  override fun showIfErrorForm(block: Login.() -> Unit) {
    //ignore
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
