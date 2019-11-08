package pt.fabm.tpl.component

interface MultiEnvTemplateServer: MultiEnvTemplate {

  override fun appendClient(text:String){
    //ignore append
  }
  override fun appendServer(text:String){
    appendable.append(text)
  }

}
