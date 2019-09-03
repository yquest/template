package pt.fabm.tpl

import pt.fabm.tpl.WithChildren.Companion.initTag

@DslMarker
annotation class HtmlTagMarker

interface WithChildren {
  val children: MutableList<ElementCreator>

  companion object {
    fun <T : ElementCreator> initTag(children: MutableList<ElementCreator>, tag: T, init: T.() -> Unit): T {
      tag.init()
      children.add(tag)
      return tag
    }
  }
}

enum class Type {
  //1st level state
  CLIENT,
  //2nd level state
  CLIENT_IMPLEMENTATION,
  //1st level state
  SERVER;

  fun toFirstLevel(): Type {
    return when (this) {
      CLIENT_IMPLEMENTATION -> CLIENT
      else -> this
    }
  }
}

interface Element {
  val children: Iterable<Element>
  fun renderTag(builder: Appendable, ident: String = "")
  val hasItem: Boolean
    get() = false
}

class ElementWrapper(
  private val prefix: String,
  private val suffix: String,
  override val children: Iterable<Element>
) : Element {
  override fun renderTag(builder: Appendable, ident: String) {
    TextElement(prefix).renderTag(builder, ident)
    for (element in children) element.renderTag(builder, "    $ident")
    TextElement(suffix).renderTag(builder, ident)
  }
}

@HtmlTagMarker
open class NoTagElement(
  override val children: Iterable<Element> = emptyList()
) : Element {

  override fun renderTag(builder: Appendable, ident: String) {
    for (child in children) {
      child.renderTag(builder, ident)
    }
  }
}

class TagElement(
  private val name: String,
  children: Iterable<Element>,
  val attributes: () -> String
) : NoTagElement(children) {

  override fun renderTag(builder: Appendable, ident: String) {
    val iterator = children.iterator()
    val noChildren = !iterator.hasNext()
    builder.append("$ident<$name${attributes()}")
    if (noChildren) builder.append("/>\n")
    else {
      builder.append(">\n")
      super.renderTag(builder, "    $ident")
      builder.append("$ident</$name>\n")
    }
  }

}

abstract class NameElementCreator(
  protected open val name: String,
  override val type: Type,
  protected open val attributes: () -> String = { "" }
) : ElementCreator, WithChildren

interface ElementCreator {
  val type: Type
  fun create(): Element
}

class LiteralClientImplementation(private val text: String, override val type: Type) : ElementCreator {
  override fun create(): Element {
    return if (type == Type.CLIENT_IMPLEMENTATION)
      TextElement(text)
    else
      NoTagElement()
  }
}

class TextElement(private val text: String) : NoTagElement() {
  override fun renderTag(builder: Appendable, ident: String) {
    builder.append("$ident$text\n")
  }
}

class TextVarCreator(
  private val serverText: () -> String,
  private val clientText: String,
  override val type: Type,
  override val children: MutableList<ElementCreator> = mutableListOf()
) :
  ElementCreator, WithChildren {
  override fun create(): Element =
    if (type == Type.SERVER) TextElement(serverText())
    else TextElement(clientText)
}


abstract class TagWithText(override val name: String, override val type: Type) : NameElementCreator(name, type) {

  operator fun String.unaryPlus() {
    val noNamedElementCreator = object : ElementCreator, WithChildren {
      override val type: Type get() = this@TagWithText.type
      override val children: MutableList<ElementCreator> = mutableListOf()
      override fun create(): Element = TextElement(this@unaryPlus)
    }
    children.add(noNamedElementCreator)
  }

  operator fun Pair<() -> String, String>.unaryPlus() {
    children.add(TextVarCreator(this.first, this.second, type))
  }

}

interface BodyTag : WithChildren, ElementCreator {
  fun <T : ElementCreator> initTag(tag: T, init: T.() -> Unit): T {
    tag.init()
    children.add(tag)
    return tag
  }

  fun table(init: Table.() -> Unit): Table = initTag(Table(type), init)

  fun <T : Component> component(component: T): T {
    children += component
    return component
  }

  fun div(id: String? = null, key: String? = null, className: String? = null, init: DIV.() -> Unit): DIV {
    val renderedAttributes = AttributeValue.render(
      type,
      AttributeValue.create { defaultAttribute("id", id) },
      AttributeValue.create { basicAttribute("key", key) },
      AttributeValue.create { className(className) })
    val div = initTag(DIV(type) { renderedAttributes }, init)
    return div
  }

  fun i(className: String? = null, init: I.() -> Unit = {}): I {
    return initTag(
      I(type) {
        AttributeValue.render(type,
          AttributeValue.create {
            className(className)
          }
        )
      },
      init
    )
  }

  fun a(href: String, onClick: String? = null, className: String? = null, init: A.() -> Unit = {}): A {
    return initTag(
      A(type) {
        AttributeValue.render(
          type,
          AttributeValue.create { basicEnsuredAttribute("href", href) },
          AttributeValue.create { className(className) },
          AttributeValue.create { clientAttribute("onClick", onClick) })
      },
      init
    )
  }

  fun scriptJS(src: String): ScriptJS {
    val scriptJS = ScriptJS(type, src)
    children += scriptJS
    return scriptJS
  }
}

class A(
  type: Type,
  override val children: MutableList<ElementCreator> = mutableListOf(),
  override val attributes: () -> String
) : TagWithText("a", type), BodyTag {
  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() },
      attributes
    )
}

class ShowIf(
  private val clause: Pair<() -> Boolean, String>,
  override val type: Type
) : BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()

  fun td(init: TD.() -> Unit): TD = initTag(children, TD(type), init)

  fun th(colSpan: Int? = null, init: TH.() -> Unit): TH = initTH(type, children, colSpan, init)

  override fun create(): Element {
    val elements = mutableListOf<Element>()
    return if (type != Type.SERVER) {
      elements += TextElement("{" + clause.second + " && (")
      elements += object : Element {
        override val children: Iterable<Element> = this@ShowIf.children.map { it.create() }
        override fun renderTag(builder: Appendable, ident: String) {
          for (child in children) {
            child.renderTag(builder, "    $ident")
          }
        }
      }
      elements += TextElement(")}")
      NoTagElement(elements)
    } else {
      object : Element {
        override val children: Iterable<Element> get() = this@ShowIf.children.map { it.create() }
        override fun renderTag(builder: Appendable, ident: String) {
          if (this@ShowIf.clause.first()) {
            NoTagElement(children).renderTag(builder, "    $ident")
          }
        }
      }
    }
  }
}

class Head(type: Type) : TagWithText("head", type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() }
    ) { "" }

  fun link(href: String, rel: String?, init: Link.() -> Unit = {}): Link {
    return initTag(
      children,
      Link(
        type
      ) {
        AttributeValue.render(type, AttributeValue.create {
          basicEnsuredAttribute("href", href)
        }, AttributeValue.create {
          basicAttribute("rel", rel)
        })
      },
      init
    )
  }
}

private fun initTH(type: Type, children: MutableList<ElementCreator>, colSpan: Int?, init: TH.() -> Unit): TH {
  val th = TH(
    type,
    AttributeValue.render(type, AttributeValue.create {
      bothValid = colSpan != null
      if (bothValid)
        setBoth("colspan", "colSpan", "\"$colSpan\"", "{$colSpan}")
    })
  )
  th.init()
  children += th
  return th
}

class TR(type: Type) : NameElementCreator("tr", type), WithChildren {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() }
    ) { "" }

  fun showIf(clause: Pair<() -> Boolean, String>, init: ShowIf.() -> Unit): ShowIf = initTag(
    children,
    ShowIf(clause, type),
    init
  )

  fun td(init: TD.() -> Unit): TD = initTag(children, TD(type), init)
  fun th(colSpan: Int? = null, init: TH.() -> Unit): TH = initTH(type, children, colSpan, init)
}

class Thead(type: Type) : NameElementCreator("thead", type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() }
    ) { "" }

  fun tr(init: TR.() -> Unit): TR = initTag(TR(type), init)
}

class Tbody(type: Type) : NameElementCreator("tbody", type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() }
    ) { "" }
}

class TD(type: Type) : TagWithText("td", type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() }
    ) { "" }
}

class TH(type: Type, private val renderedAttributes: String = "") : TagWithText("th", type) {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() }
    ) { renderedAttributes }
}

class Table(type: Type) : NameElementCreator("table", type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()
  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() }
    ) { "" }

  fun thead(init: Thead.() -> Unit): Thead = initTag(Thead(type), init)
  fun tbody(init: Tbody.() -> Unit): Tbody = initTag(Tbody(type), init)
}


class Body(type: Type) : TagWithText("body", type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()
  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() }
    ) { "" }
}

class CommentServerTag(private val text: String, override val type: Type) : ElementCreator, WithChildren {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element {
    return if (type == Type.SERVER)
      TextElement("<!--$text-->")
    else
      NoTagElement()
  }
}

open class Component(name: String, type: Type) : TagWithText(name, type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element {
    return when (type) {
      Type.SERVER -> NoTagElement(
        children.map { it.create() }
      )
      Type.CLIENT_IMPLEMENTATION -> NoTagElement(
        children.map { it.create() }
      )
      else -> TagElement(
        name,
        listOf(),
        attributes
      )
    }
  }
}

class DIV(type: Type, override val attributes: () -> String) : TagWithText("div", type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() },
      attributes
    )
}

class I(type: Type, override val attributes: () -> String) : TagWithText("i", type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() },
      attributes
    )
}

class Link(type: Type, override val attributes: () -> String) : TagWithText("link", type) {
  override val children: MutableList<ElementCreator> = mutableListOf()

  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() },
      attributes
    )
}

class ScriptJS(type: Type, private val src: String) : TagWithText(name = "script", type = type) {
  override val children: MutableList<ElementCreator> get() = error("cannot have children")
  override fun create(): Element {
    if (type != Type.SERVER) error("a script element can be only a server type")
    return TagElement(name, emptyList()) {
      AttributeValue.render(
        type,
        AttributeValue.create { basicAttribute("type", "text/javascript") },
        AttributeValue.create { basicAttribute("src", src) }
      )
    }
  }
}

class HTML(type: Type) : TagWithText("html", type), BodyTag {
  override val children: MutableList<ElementCreator> = mutableListOf()
  override fun create(): Element =
    TagElement(
      name,
      children.map { it.create() }
    ) { "" }

  fun head(init: Head.() -> Unit) = initTag(Head(type), init)
  fun body(init: Body.() -> Unit) = initTag(Body(type), init)
}

fun html(type: Type, init: HTML.() -> Unit): HTML {
  val html = HTML(type)
  html.init()
  return html
}
