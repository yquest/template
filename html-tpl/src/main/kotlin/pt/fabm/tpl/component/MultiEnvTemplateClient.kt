package pt.fabm.tpl.component

interface MultiEnvTemplateClient: MultiEnvTemplate {

  override fun appendClient(text:String){
    buffer.appendString(text)
  }
  override fun appendServer(text:String){
    //ignore append
  }

}
