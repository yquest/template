package pt.fabm.tpl.test

interface MultiEnvTemplate {

  fun appendClient(text:String)
  fun appendServer(text:String)
  fun appendClassName(className:String){
    appendServer(""" class=$className""")
    appendClient(""" className=$className""")
  }

}
