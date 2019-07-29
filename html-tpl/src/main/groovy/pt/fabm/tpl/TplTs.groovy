package pt.fabm.tpl

class TplTs {
  interface Entry{
    void rendered(ComponentTs componentTs);
  }
  private def xml


  TplTs load(String resourcePath){
    xml = new XmlParser().parse(getClass().getResourceAsStream(resourcePath))
    xml.imports.import.each{
      TplTs tplTs = new TplTs()
      tplTs.load(it.file)
    }
    return this
  }



  void render(Entry entry){

  }
}
