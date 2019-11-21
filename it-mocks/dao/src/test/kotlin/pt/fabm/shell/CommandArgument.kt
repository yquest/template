package pt.fabm.shell

import io.vertx.core.Handler
import io.vertx.reactivex.ext.shell.command.CommandProcess

class CommandArgument(override val node: String, val handler: (CommandProcess)->Unit):LevelArgument {
  override val isTerminal: Boolean = true
}
