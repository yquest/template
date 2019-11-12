package pt.fabm.tpl.component

import io.vertx.core.buffer.Buffer

interface MultiEnvTemplate {

  val buffer:Buffer
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
