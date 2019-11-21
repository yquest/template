package pt.fabm.shell

import io.vertx.reactivex.ext.shell.command.CommandBuilder

interface LevelArgument {
  companion object {
    fun root(node: String, init: ParentArgument.() -> Unit):CommandBuilder {
      val parentArgument = ParentArgument(node)
      parentArgument.init()
      return parentArgument.createCommandBuilder()
    }
  }

  val isTerminal: Boolean
  val node: String
}
