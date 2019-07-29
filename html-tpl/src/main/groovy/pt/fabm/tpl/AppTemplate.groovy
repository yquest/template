package pt.fabm.tpl

def frontend = './frontend/src/components/gen' as File

def listCarsTpl = new XmlParser().parse(getClass().getResourceAsStream("/listCars.tpl.xml"))

TplTs tplTs = new TplTs()
tplTs
  .load("/listCars.tpl.xml")

