package pt.fabm.shell

import io.vertx.core.Handler
import io.vertx.core.cli.Argument
import io.vertx.reactivex.core.cli.CLI
import io.vertx.reactivex.ext.shell.command.Command
import io.vertx.reactivex.ext.shell.command.CommandBuilder

class ValuesLevelArgument(val node:String, override val level: Int, val values: MutableList<LevelArgument> = mutableListOf()):LevelArgument {
  override val isString: Boolean = false
  override val isList: Boolean = true

  fun addFinalNode(node: String, handler: Handler<Command>) {
    values.add(StringLevelArgument(level+1, node, handler))
  }

  fun addNode(node:String):ValuesLevelArgument{
    val values = ValuesLevelArgument(node,level+1)
    this.values.add(values)
    return values
  }

  fun build(){
    val cli = CLI.create(node)
    val command = CommandBuilder.command(cli)
    command.completionHandler { completion ->

      val possibilities = listOf("clean", "insert", "delete")
      val lastToken = completion.lineTokens().last().value()

      val candidates = possibilities.filter { it.startsWith(lastToken) }.let {
        if (it.isEmpty()) possibilities else it
      }

      if (completion.lineTokens().size < 3) {
        if (candidates.size == 1) completion.complete(candidates.get(0).substring(lastToken.length), true)
        else completion.complete(candidates)
      }else{
        completion.complete(emptyList())
      }
    }
    command.processHandler { process ->

      val commandLine = process.commandLine()
      val arg:String = commandLine.getArgumentValue(0)
      println(""" value:$arg """)
      process.end()
    }

  }
}
