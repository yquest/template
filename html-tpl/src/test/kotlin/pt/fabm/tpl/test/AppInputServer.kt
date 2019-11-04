package pt.fabm.tpl.test

class AppInputServer(appendable: Appendable) : AppInput(appendable) {
  override fun appendClient(text: String) {
    //ignore on server
  }

  override fun appendServer(text: String) {
    appendable.append(text)
  }

}
