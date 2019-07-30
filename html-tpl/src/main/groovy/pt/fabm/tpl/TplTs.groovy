package pt.fabm.tpl

class TplTs {
  private def xml
  List<TplTs> imports = []

  TplTs load(String resourcePath){
    xml = new XmlParser().parse(getClass().getResourceAsStream(resourcePath))
    xml.imports.import.each{
      TplTs tplTs = new TplTs()
      imports += tplTs.load(it.@file)
    }
    return this
  }

  String render(){

  }
}
