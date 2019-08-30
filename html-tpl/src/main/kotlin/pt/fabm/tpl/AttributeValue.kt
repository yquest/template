package pt.fabm.tpl


class AttributeValue(
  var clientKey: String = "",
  var clientValue: String = "",
  var serverKey: String = "",
  var serverValue: String = "",
  var isClientValid: Boolean = true,
  var isServerValid: Boolean = true
) {
  companion object {
    fun create(init: AttributeValue.() -> Unit): AttributeValue {
      val attributeValue = AttributeValue()
      attributeValue.init()
      return attributeValue
    }

    fun render(type: Type, vararg attributes: AttributeValue): String {
      val sb = StringBuilder()

      fun appendAttribute(key: String, value: String) {
        sb.append(" $key=$value")
      }

      for (av in attributes) {
        if (type == Type.SERVER) {
          if (av.serverKey.isNotEmpty() && av.isServerValid) {
            appendAttribute(av.serverKey, av.serverValue)
            continue
          }
        } else if (av.clientKey.isNotEmpty() && av.isClientValid) {
          appendAttribute(av.clientKey, av.clientValue)
          continue
        }
      }
      return sb.toString()
    }
  }

  var bothValid
    get() = isServerValid && isClientValid
    set(value) {
      this.isServerValid = value
      this.isClientValid = value
    }

  fun defaultAttribute(key: String, value: String?) {
    clientKey = key
    serverKey = key
    clientValue = "{$value}"
    serverValue = "\"$value\""
    isClientValid = value != null
    isServerValid = value != null
  }

  fun setBoth(serverKey: String, clientKey: String, serverValue: Any?, clientValue: Any?) {
    this.serverKey = serverKey
    this.clientKey = clientKey
    this.serverValue = serverValue.toString()
    this.clientValue = clientValue.toString()
  }

  fun setBoth(serverKey: String, clientKey: String, value: Any?) {
    this.serverKey = serverKey
    this.clientKey = clientKey
    this.serverValue = value.toString()
    this.clientValue = value.toString()
  }

  fun clientAttribute(key: String, value: String?) {
    clientKey = key
    clientValue = "{$value}"
    isClientValid = value != null
    isServerValid = false
  }

  fun serverAttribute(key: String, value: String?) {
    clientKey = key
    clientValue = "{$value}"
    isClientValid = true
    isServerValid = false
  }

  fun basicEnsuredAttribute(key: String, value: String) {
    clientKey = key
    serverKey = key
    clientValue = "\"$value\""
    serverValue = clientValue
    isClientValid = true
    isServerValid = true
  }

  fun basicAttribute(key: String, value: String?) {
    clientKey = key
    serverKey = key
    clientValue = "\"$value\""
    serverValue = clientValue
    isClientValid = value != null
    isServerValid = value != null
  }

  fun className(value: String?) {
    if (value != null) {
      clientKey = "className"
      serverKey = "class"
      clientValue = "\"$value\""
      serverValue = clientValue
      isClientValid = true
      isServerValid = true
    }
  }
}
