package pt.fabm.shell

import io.vertx.core.Handler
import io.vertx.reactivex.ext.shell.command.Command

class StringLevelArgument(override val level: Int, val value: String, val handler: Handler<Command>):LevelArgument {
  override val isString: Boolean = true
  override val isList: Boolean = false
}
