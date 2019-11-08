package pt.fabm.tpl.component

interface MultiEnvTemplate {

  val appendable:Appendable
  fun appendClient(text:String)
  fun appendServer(text:String)
  fun appendClassName(className:String){
    appendServer(""" class="$className"""")
    appendClient(""" className="$className"""")
  }

  fun appendTabIndex(tabIndex:Any){
    appendServer(""" tabindex="$tabIndex"""")
    appendClient(""" tabIndex={$tabIndex}""")
  }

}
