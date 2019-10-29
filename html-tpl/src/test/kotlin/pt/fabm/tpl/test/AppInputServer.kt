package pt.fabm.tpl.test

class AppInputServer(appendable: Appendable) : AppInput(appendable) {
  override val helper: Helper = HelperClient()
}
